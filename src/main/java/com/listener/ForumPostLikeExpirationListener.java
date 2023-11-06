package com.listener;

import com.alibaba.fastjson2.JSONObject;
import com.mapper.ForumMapper;
import com.pojo.ForumPostLike;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * TODO：此方法过于依赖redis
 * 若redis宕机，则会导致数据丢失，后续会进行修改
 * 改为消息队列
 */
@Slf4j
@Component
public class ForumPostLikeExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private ForumMapper forumMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    public ForumPostLikeExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

//    @Override
//    protected void doRegister(RedisMessageListenerContainer listenerContainer) {
//        // 添加消息监听适配器，并指定订阅的主题为 "post_like:*__keyevent@0__:expired"
//        // 这里是订阅以 "post_like:" 开头，以 "__keyevent@0__:expired" 结尾的频道
//        listenerContainer.addMessageListener(this, new PatternTopic("post_like:*__keyevent@0__:expired"));
//    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        log.info("键：{} 过期", expiredKey);
        if (!expiredKey.startsWith(RedisConstant.FORUM_POST_LIKE)) {
            return;
        }
        //获取帖子id
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
