package com.redis.handler;

import com.alibaba.fastjson2.JSONObject;
import com.mapper.ForumMapper;
import com.pojo.ForumPostLike;
import com.redis.RedisExpiredKeyHandler;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ForumPostLikeExpiredKeyHandler implements RedisExpiredKeyHandler {

    private ForumMapper forumMapper;

    private RedisUtil redisUtil;

    public ForumPostLikeExpiredKeyHandler(RedisUtil redisUtil, ForumMapper forumMapper) {
        this.redisUtil = redisUtil;
        this.forumMapper = forumMapper;
    }

    @Override
    public void handleExpiredKey(String expiredKey) {
//        获取帖子id
        Integer id = Integer.parseInt(expiredKey.substring(RedisConstant.FORUM_POST_LIKE.length()));
        //获取备份key
        String backupKey = RedisConstant.BACKUP_FORUM_POST_LIKE + id;
        try {
            // 根据过期的 key 从 Redis 中获取对应的数据
            Map<String, Object> map = redisUtil.hmget(backupKey);
            log.info("map:{}", map);
            if (map != null) {
                //修改帖子的点赞数量
                forumMapper.updateForumPostLikeNum(map.size(), id);
                log.info("修改帖子：{} 的点赞数量：{}", id, map.size());
                //提取数据转化为ForumPostLike的list类型
                List<ForumPostLike> list = extractForumPostLikeToList(map);
                //批量添加数据到点赞表中
                forumMapper.insertList2ForumPostLike(list);
                //添加到消息表中
                for (ForumPostLike like : list) {
                    forumMapper.insertForumMessage(like,"like");
                }
                log.info("批量添加数据到点赞表中:{}", list);
            }
            redisUtil.del(backupKey);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            log.info("{} 将过期key的数据存入数据库失败", expiredKey);
        }
    }

    private List<ForumPostLike> extractForumPostLikeToList(Map<String, Object> map) {
        List<ForumPostLike> list = new ArrayList<>();
        //遍历将JSON字符串转化为ForumPostLike对象
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            ForumPostLike forumPostLike = JSONObject.parseObject(String.valueOf(value), ForumPostLike.class);
            list.add(forumPostLike);
        }
        return list;
    }
}