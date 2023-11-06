package com.kafka;

import com.alibaba.fastjson2.JSONObject;
import com.handler.MessageHandler;
import com.handler.MessageHandlerFactory;
import com.mapper.ForumMapper;
import com.pojo.ForumPost;
import com.pojo.ForumPostCollect;
import com.pojo.ForumPostComment;
import com.pojo.ForumPostLike;
import com.utils.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka消费者
 */
@Slf4j
@Component
public class KafkaForumConsumer {

    @Autowired
    private ForumMapper forumMapper;

    @Autowired
    MessageHandlerFactory messageHandlerFactory;


    @KafkaListener(topics = "post", groupId = "find-me")
    public void handleMessage(ConsumerRecord<String, String> message) {
        String key = message.key();
        String value = message.value();
        log.info("消息队列的数据：{}", message);
        log.info("消息类型：{}", key);
        if (key.startsWith(KafkaConstant.POST_HEADER)) {
            handlerMsg(key,value,ForumPost.class);
        } else if (key.startsWith(KafkaConstant.POST_LIKE_HEADER)) {
            handlerMsg(key,value,ForumPostLike.class);
        } else if (key.startsWith(KafkaConstant.POST_COLLECT_HEADER)) {
            handlerMsg(key, value, ForumPostCollect.class);
        } else if (key.startsWith(KafkaConstant.POST_COMMENT_HEADER)) {
            handlerMsg(key, value, ForumPostComment.class);
        } else {
            log.info("标识符不匹配！");
        }
    }

    private <T> void handlerMsg(String key, String value, Class<T> clazz) {
        T t = JSONObject.parseObject(value, clazz);
        MessageHandler<T> messageHandler = (MessageHandler<T>) messageHandlerFactory.createMessageHandler(key);
        messageHandler.handle(t, forumMapper);
    }


}
