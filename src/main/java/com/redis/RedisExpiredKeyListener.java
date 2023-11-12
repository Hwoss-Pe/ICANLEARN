package com.redis;

import com.mapper.ForumMapper;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.function.Supplier;


@Component
public  class RedisExpiredKeyListener extends KeyExpirationEventMessageListener {


    @Autowired
    private ExpiredKeyHandlerFactory factory;

    @Autowired
    public RedisExpiredKeyListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        System.out.println("键：" + expiredKey + " 过期");
//        Map<Class<?>, Supplier<RedisExpiredKeyHandler>> handlerRegistry = factory.getHandlerRegistry();
            for (Map.Entry<String, Class<?>> classEntry : ExpiredKeyHandlerFactory.map.entrySet()) {
                if(expiredKey.startsWith(classEntry.getKey())) {
                    RedisExpiredKeyHandler handler = factory.createHandler(classEntry.getValue());
                    handler.handleExpiredKey(expiredKey);
                }
            }
    }


}