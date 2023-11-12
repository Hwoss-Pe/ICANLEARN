package com.kafka;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class KafkaProducer {
    @Value("${spring.kafka.producer.properties.max.retries}")
    private Integer retries;

    private Integer tryTimes;
    private String topic;
    private String key;
    private String messageJson;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


        private final ListenableFutureCallback<SendResult<String, Object>> callback = new ListenableFutureCallback<SendResult<String, Object>>() {


            @Override
            public void onSuccess(SendResult<String, Object> result) {
                // 处理消息发送成功的情况
                if (result != null) {
                    log.info("Message sent successfully to topic: " + result.getRecordMetadata().topic());
                }else {
                    log.info("Topic of this message is null");
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                ex.printStackTrace();
                // 处理发送消息过程中的异常
                log.info("Error occurred while sending message: " + ex.getMessage());
                tryTimes--;
                retrySend(topic, key, messageJson, tryTimes);
            }
        };

        public <T> void sendMessage(String topic, String key, T message) {
            tryTimes = retries;
            String messageJson = JSONObject.toJSONString(message);
            this.topic = topic;
            this.key = key;
            this.messageJson = messageJson;
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, messageJson);
            future.addCallback(callback);
        }

            private <T> void retrySend(String topic, String key, T message, Integer tryTimes) {
                if (tryTimes >= 0) {
                    log.info("第{}次重试...", retries - tryTimes);
                    ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
                    future.addCallback(callback);
                } else {
                    log.info("已达最大重试次数：{}...", retries);
                }
            }
}