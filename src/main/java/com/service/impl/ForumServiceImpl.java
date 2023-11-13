package com.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.kafka.KafkaProducer;
import com.mapper.ForumMapper;
import com.pojo.*;
import com.service.ForumService;
import com.utils.BeanMapUtils;
import com.utils.Identification;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.utils.KafkaConstant.*;

@Slf4j
@Service
public class ForumServiceImpl implements ForumService {

    @Autowired
    private ForumMapper forumMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    private final RedisUtil redisUtil;

    @Autowired
    public ForumServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    //上传帖子
    @Transactional
    @Override
    public Integer uploadPost(ForumPost forumPost) {
        //获取当前计数器的自增id
        Integer incrementId = getPostIncrementId();
        //定义key
        String postKey = getPostKey(incrementId);
        //设置ForumPost对象的id
        forumPost.setId(incrementId);
        //转为map
        Map<String, Object> forumPostMap = BeanMapUtils.beanToMap(forumPost);
        //存入redis
        redisUtil.hmset(postKey, forumPostMap, RedisConstant.FORUM_POST_EXPIRE_TIME);
        //发送到消息队列中
        kafkaProducer.sendMessage(FORUM_TOPIC, INSERT_POST, forumPost);
        //返回帖子的id
        return incrementId;
    }

    //修改帖子开放状态
    @Transactional
    @Override
    public boolean updateStatus(Integer status, Integer publisherId, String postId) {
        if (!judgePostIsValid(postId)) {
            return false;
        }
        String postKey = getPostKey(postId);
        if (!judgePublisher(postId, publisherId)) {
            return false;
        }
        //更新标志
        boolean updated;
        if (redisUtil.hasKey(postKey)) {
            //修改redis上帖子的可见范围
            updated = redisUtil.hset(postKey, "visibleScope", status);
        } else {
            //查询数据库中
            ForumPost forumPost = forumMapper.selectForumPostByPostId(postId);
            //修改可见范围
            forumPost.setVisibleScope(status);
            //转为map
            Map<String, Object> map = BeanMapUtils.beanToMap(forumPost);
            //将其保存在redis中
            updated = redisUtil.hmset(postKey, map, RedisConstant.FORUM_POST_EXPIRE_TIME);
        }
        //处理数据
        try {
            if (updated) {
                //处理数据
                ForumPost forumPost = new ForumPost();
                forumPost.setVisibleScope(status);
                forumPost.setId(Integer.parseInt(postId));
                //发送消息到消息队列中
                kafkaProducer.sendMessage(FORUM_TOPIC, UPDATE_POST_VISIBLE_SCOPE, forumPost);
            }
        } catch (Exception e) {
            //若有异常则再设置为false
            e.printStackTrace();
            updated = false;
        }
        //返回结果
        return updated;
    }

    //删除帖子
    @Transactional
    @Override
    public boolean deletePost(Integer publisherId, String postId) {
        String postKey = getPostKey(postId);
        boolean judgePublisher = judgePublisher(postId, publisherId);
        if (!judgePublisher) {
            return false;
        }
        Integer postIdi = Integer.parseInt(postId);
        //若删除的帖子是表中最大id，则可能影响到发布帖子id在redis和mysql数据库的一致性
        if (redisUtil.hasKey(RedisConstant.POST_AUTO_INCREMENT_ID) && postIdi.equals(forumMapper.selectIncrementId(ForumMapper.POST))) {
            redisUtil.decr(RedisConstant.POST_AUTO_INCREMENT_ID, 1);
        }
        try {
            //直接删除缓存中的数据
            redisUtil.del(postKey);
            //处理数据
            ForumPost forumPost = new ForumPost();
            forumPost.setId(Integer.parseInt(postId));
            //发送删除数据到消息队列
            kafkaProducer.sendMessage(FORUM_TOPIC, DELETE_POST, forumPost);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    //获取指定数量的帖子预览
    @Override
    public List<ForumPostPreview> getNumPostPreviews(Integer userId, String num) {
        String previewKey = RedisConstant.POST_PREVIEWS_IDS + userId;
        long lNum = Long.parseLong(num);
        if (!redisUtil.hasKey(previewKey) || redisUtil.lGetListSize(previewKey) < lNum) {
            //查询该用户可查看的有效帖子
            List<Integer> list = forumMapper.selectForumPostIdsByUserId(userId);
            redisUtil.del(previewKey);
            redisUtil.lSetList(previewKey, list, RedisConstant.POST_PREVIEWS_IDS_EXPIRE_TIME);
        }
        long size = redisUtil.lGetListSize(previewKey);
        if (size == 0) {
            return null;
        }
        List<Integer> idList = redisUtil.lRemove(previewKey, lNum);

        return forumMapper.selectForumPostPreViewsByIds(idList);
    }


    //(取消)点赞帖子
    @Transactional
    @Override
    public Integer likePost(Integer userId, String postId) {
        Integer status = 1;
        try {
            //判断该帖子是否有效
            if (!judgePostIsValid(postId)) {
                status = -1;
            }
            String postKey = getPostKey(postId);
            //定义点赞帖子的key
            String likeKey = RedisConstant.FORUM_POST_LIKE + postId;
            //定义点赞帖子数据备份的key
            String backupLikeKey = RedisConstant.BACKUP_FORUM_POST_LIKE + postId;

            //若存在likeKey的userId，即在redis中查询到用户点赞了，则返回点赞失败
            if (redisUtil.hHasKey(likeKey, String.valueOf(userId))) {
                status = 0;
            } else {
                //查询数据库中该帖子的点赞用户
                List<Integer> userIdList = forumMapper.selectLikeUserIdByPostId(postId);
                //若包含该用户id则返回false
                if (userIdList.contains(userId)) {
                    status = 0;
                }
            }
            if (status.equals(0)) {//用户已点赞，需取消赞
                //删除redis中的key
                redisUtil.hdel(likeKey, String.valueOf(userId));
                redisUtil.hdel(backupLikeKey, String.valueOf(userId));
                if (redisUtil.hasKey(postKey)) {
                    redisUtil.hdecr(postKey, "likeNum", 1);
                }
                //处理数据
                ForumPostLike like = new ForumPostLike();
                like.setUserId(userId);
                like.setPostId(Integer.parseInt(postId));
                //发送到消息队列
                kafkaProducer.sendMessage(FORUM_TOPIC, DELETE_POST_LIKE, like);
            } else if (status.equals(1)) {//确认用户还未点赞
                if (redisUtil.hasKey(postKey)) {
                    redisUtil.hincr(postKey, "likeNum", 1);
                }
                //处理数据
                ForumPostLike forumPostLike = new ForumPostLike();
                forumPostLike.setPostId(Integer.parseInt(postId));
                forumPostLike.setUserId(userId);
                //转为JSON字符串
                String forumPostLikeJson = JSONObject.toJSONString(forumPostLike);
                //添加到redis，并设置过期时间，交由后续的过期key监听存储数据到数据库中
                redisUtil.hset(likeKey, String.valueOf(userId), forumPostLikeJson, RedisConstant.FORUM_POST_LIKE_COLLECT_EXPIRE_TIME);
//                redisUtil.hset(likeKey, String.valueOf(userId), forumPostLikeJson, 15000);
                redisUtil.hset(backupLikeKey, String.valueOf(userId), forumPostLikeJson);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            status = -1;
        }
        return status;
    }

    //收藏帖子
    @Transactional
    @Override
    public Integer collectPost(Integer userId, String postId) {
        Integer status = 1;
        String postKey = getPostKey(postId);
        //判断是否为发布者以及帖子是否有效
        if (judgePublisher(postId, userId) || !judgePostIsValid(postId)) {
            return -1;
        }
        String collectKey = RedisConstant.FORUM_POST_COLLECT + postId;
        try {
            if (redisUtil.sHasKey(collectKey, userId)) {
                status = 0;
            } else {
                //查询数据库中该帖子的收藏用户
                List<Integer> userIdList = forumMapper.selectCollectUserIdByPostId(postId);
                //若包含该用户id则返回false
                if (userIdList.contains(userId)) {
                    status = 0;
                }
            }
            if (status.equals(0)) {//用户已收藏，要取消收藏
                if (redisUtil.hasKey(postKey)) {
                    redisUtil.hdecr(postKey, "collectNum", 1);
                }
                //删除redis中的key
                redisUtil.setRemove(collectKey, userId);
                //处理数据
                ForumPostCollect collect = new ForumPostCollect();
                collect.setUserId(userId);
                collect.setPostId(Integer.parseInt(postId));
                //发送到消息队列
                kafkaProducer.sendMessage(FORUM_TOPIC, DELETE_POST_COLLECT, collect);
            } else if (status.equals(1)) {//确认用户还未收藏
                if (redisUtil.hasKey(postKey)) {
                    redisUtil.hincr(postKey, "collectNum", 1);
                }
                //存入redis
                redisUtil.set(collectKey, userId, RedisConstant.FORUM_POST_LIKE_COLLECT_EXPIRE_TIME);
                //处理数据
                ForumPostCollect collect = new ForumPostCollect();
                collect.setPostId(Integer.parseInt(postId));
                collect.setUserId(userId);
                kafkaProducer.sendMessage(FORUM_TOPIC, INSERT_POST_COLLECT, collect);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            status = -1;
        }
        return status;
    }

    //获取用户的收藏预览表
    @Override
    public List<ForumPostPreview> getCollectList(Integer userId) {
        return forumMapper.selectCollectPostPreviewsByUserId(userId);
    }

    //获取用户的点赞预览表
    @Override
    public List<ForumPostPreview> getLikeList(Integer userId) {
        return forumMapper.selectLikePostPreviewsByUserId(userId);
    }

    //评论帖子
    @Transactional
    @Override
    public Integer commentPost(ForumPostComment forumPostComment) {
        Integer postId = forumPostComment.getPostId();
        String postKey = getPostKey(postId);
        Integer commentId = getCommentIncrementId();
        forumPostComment.setId(commentId);
        if (redisUtil.hasKey(postKey)) {
            redisUtil.hincr(postKey, "comment_num", 1);
        }
        kafkaProducer.sendMessage(FORUM_TOPIC, INSERT_POST_COMMENT, forumPostComment);
        //返回评论id
        return commentId;
    }


    //删除评论
    @Transactional
    @Override
    public boolean deletePostComment(Integer userId, String commentId) {
        //判断是否为操作者的评论
        Integer realUserId = forumMapper.selectCommentUserIdByCommentId(commentId);
        if (!userId.equals(realUserId)) {
            return false;
        }
        try {
            Integer commentIdi = Integer.parseInt(commentId);
            //若删除的评论是表中最大id，则可能影响到新增评论id在redis和mysql数据库的一致性
            if (redisUtil.hasKey(RedisConstant.COMMENT_AUTO_INCREMENT_ID) && commentIdi.equals(forumMapper.selectIncrementId(ForumMapper.POST_COMMENTS))) {
                redisUtil.decr(RedisConstant.COMMENT_AUTO_INCREMENT_ID, 1);
            }
            //处理数据
            ForumPostComment forumPostComment = new ForumPostComment();
            forumPostComment.setId(commentIdi);
            //发送到消息队列
            kafkaProducer.sendMessage(FORUM_TOPIC, DELETE_POST_COMMENT, forumPostComment);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    //搜索，返回匹配的帖子预览对象
    @Override
    public List<ForumPostPreview> searchKeyWords(Integer userId, String keywords) {
        List<Integer> list = forumMapper.selectIdsBySearchContent(keywords);
        if (list == null) {
            return null;
        }
        return calibrateAndProcessIds(list, userId);
    }

    //获取该用户发布的帖子
    @Override
    public List<ForumPostPreview> getPublishedPosts(Integer userId) {
        return forumMapper.selectPublishedPostPreViewsByUserId(userId);
    }

    //获取职业探索帖子预览
    @Override
    public List<OccupationPersonPreview> getOccupationPersonPreviews(Integer num) {
        return forumMapper.selectNumOccupationPersonPreviews(num);
    }

    //获取职业探索帖子人物详细信息
    @Override
    public OccupationPerson getOccupationPersonDetail(Integer id) {
        return forumMapper.selectOccupationPerson(id);
    }


    //点赞职业探索帖子人物
    @Override
    @Transactional
    public Integer likeOccupationPerson(Integer userId,Integer id) {
        try {
            Integer likeId = forumMapper.selectOccupationPersonLikeByUserId(userId,id);
            if (likeId == null){
                //如果不存在点赞信息则未点赞
                forumMapper.insertOccupationPersonLike(userId,id);
                forumMapper.updateOccupationPersonLike(1,id);
                return 1;
            }else {
                forumMapper.deletePostLike(userId,id);
                forumMapper.updateOccupationPersonLike(-1,id);
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //收藏职业探索帖子人物
    @Override
    @Transactional
    public Integer collectOccupationPerson(Integer userId,Integer id) {
        try {
            Integer collect = forumMapper.selectOccupationPersonCollectById(userId,id);
            if (collect == null){
                //如果不存在收藏信息则未收藏
                forumMapper.insertOccupationPersonCollect(userId,id);
                forumMapper.updateOccupationPersonCollect(1,id);
                return 1;
            }else {
                forumMapper.deletePostCollect(userId,id);
                forumMapper.updateOccupationPersonCollect(-1,id);
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //获取职业探索人物帖子的评论
    @Override
    public List<ForumPostComment> getCommentsById(String id) {
        List<ForumPostComment> commentList = forumMapper.selectForumPostCommentsByPostId(id);
        return sortOutComments(commentList);
    }

    //获取postId对应的帖子详细信息
    @Override
    public ForumPost getPostDetails(Integer userId, String postId) {
        String postKey = getPostKey(postId);
        //判断帖子是否有效
        if (judgePostIsValid(postId)) {
            ForumPost forumPost;
            //获取帖子信息
            if (redisUtil.hasKey(postKey)) {
                Map<String, Object> map = redisUtil.hmget(postKey);
                forumPost = BeanMapUtils.mapToBean(map, ForumPost.class);
            } else {
                forumPost = forumMapper.selectForumPostByPostId(postId);
            }
            //判断是否为自己可见
            if (forumPost.getVisibleScope().equals(Identification.PRIVATE_VISIBLE) && !judgePublisher(postId, userId)) {
                return null;
            }
            //获取帖子的评论
            if (forumPost.getComments() == null) {
                List<ForumPostComment> commentList = forumMapper.selectForumPostCommentsByPostId(postId);
                //若不为null则整理数据
                if (commentList != null) {
                    List<ForumPostComment> comments = sortOutComments(commentList);
                    forumPost.setComments(comments);
                }
            }
            //存入redis
            Map<String, Object> map = BeanMapUtils.beanToMap(forumPost);
            redisUtil.del(postKey);
            redisUtil.hmset(postKey, map, RedisConstant.FORUM_POST_EXPIRE_TIME);

            return forumPost;
        }
        return null;
    }

    //校对多个id是否有效并返回预览对象
    private List<ForumPostPreview> calibrateAndProcessIds(List<Integer> ids, Integer userId) {
        List<ForumPostPreview> previews = forumMapper.selectForumPostPreViewsByIds(ids);
        List<ForumPostPreview> result = new ArrayList<>();
        for (ForumPostPreview preview : previews) {
            Integer publisherId = preview.getPublisherId();
            LocalDateTime createTime = preview.getCreateTime().toLocalDateTime();
            if (userId.equals(publisherId) || createTime.isAfter(LocalDateTime.now())) {
                continue;
            }
            result.add(preview);
        }
        return result;
    }


    //整理帖子评论
    private List<ForumPostComment> sortOutComments(List<ForumPostComment> comments) {
        //存储子级评论
        Map<Integer, List<ForumPostComment>> commentsMap = new HashMap<>();
        //存储最高级评论的map
        Map<Integer, ForumPostComment> parentMap = new HashMap<>();
        //将list中的评论按照父级id分类转为map
        for (ForumPostComment comment : comments) {
            //父级评论
            if (comment.getParentCommentId() == null) {
                Integer id = comment.getId();
                parentMap.put(id, comment);
                continue;
            }
            //子级评论
            Integer parentCommentId = comment.getParentCommentId();
            commentsMap.computeIfAbsent(parentCommentId, k -> new ArrayList<>());
            commentsMap.get(parentCommentId).add(comment);
        }

        //遍历子级评论map，赋值给对应父级评论
        for (Map.Entry<Integer, List<ForumPostComment>> entry : commentsMap.entrySet()) {
            Integer key = entry.getKey();
            List<ForumPostComment> value = entry.getValue();
            if (parentMap.containsKey(key)) {
                ForumPostComment forumPostComment = parentMap.get(key);
                forumPostComment.setSubComments(value);
            }
        }
        List<ForumPostComment> result = new ArrayList<>();
        //获取value列表
        for (Map.Entry<Integer, ForumPostComment> entry : parentMap.entrySet()) {
            result.add(entry.getValue());
        }
        //按照时间排序
        result.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        return result;
    }

    //判断帖子有效
    private boolean judgePostIsValid(String postId) {
        ForumPost forumPost = forumMapper.selectForumPostByPostId(postId);
        log.info("判断帖子是否有效id:{}", postId);
        //首先判断获取到的帖子对象是否为null
        if (forumPost != null) {
            //将当前时间与帖子创建时间对比
            Timestamp createTime = forumPost.getCreateTime();
            Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
            //若当前时间比创建时间晚，则帖子有效
            return currentTimeStamp.after(createTime);
        }
        log.info("找不到该帖子");
        return false;
    }

    //判断帖子的作者是否为操作者
    private boolean judgePublisher(String postId, Integer userId) {
        log.info("判断帖子的作者是否为操作者,帖子id：{} 操作者id：{}", postId, userId);
        String postKey = getPostKey(postId);
        //判断redis是否存在该键
        boolean hasKey = redisUtil.hasKey(postKey);
        Integer realPublisherId;
        if (hasKey) {
            //获取帖子发布者id
            realPublisherId = (Integer) redisUtil.hget(postKey, "publisherId");
        } else {
            int id = Integer.parseInt(postId);
            //读取数据库获取
            realPublisherId = forumMapper.selectPublisherIdByPostId(id);
        }
        return userId.equals(realPublisherId);
    }


    //获取帖子的自增id
    private Integer getPostIncrementId() {
        return getIncrementId(RedisConstant.POST_AUTO_INCREMENT_ID, ForumMapper.POST);
    }

    //获取帖子评论的自增id
    private Integer getCommentIncrementId() {
        return getIncrementId(RedisConstant.COMMENT_AUTO_INCREMENT_ID, ForumMapper.POST_COMMENTS);
    }

    //获取指定表
    private Integer getIncrementId(String identification, String tableName) {
        //查询redis是否含有该自增计数器
        if (redisUtil.hasKey(identification)) {
            Integer id = (Integer) redisUtil.get(identification);
            //自增1便于下次使用
            redisUtil.incr(identification, 1);
            return id;
        }
        //查询数据库中id的最大值,即最后一个帖子数据的自增id
        Integer incrementId = forumMapper.selectIncrementId(tableName);
        //若表中无数据即返回null,设置为0
        if (incrementId == null) {
            incrementId = 0;
        }
        //++用于当前的postId
        incrementId++;
        //+1存入缓存用于下次使用
        redisUtil.set(identification, incrementId + 1, RedisConstant.INCREMENT_ID_EXPIRE_TIME);
        return incrementId;
    }


    private String getPostKey(String postId) {
        return RedisConstant.FORUM_POST + postId;
    }

    private String getPostKey(Integer postId) {
        return RedisConstant.FORUM_POST + postId;
    }
}
