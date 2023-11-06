package com.kafka;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public <T>void sendMessage(String topic, String key, T message, Integer partitions) {
        String messageJson = JSONObject.toJSONString(message);
        kafkaTemplate.send(topic, partitions, key, messageJson);
    }

    public <T> void sendMessage(String topic, String key, T message){
        String messageJson = JSONObject.toJSONString(message);
        kafkaTemplate.send(topic,key,messageJson);
    }


}
