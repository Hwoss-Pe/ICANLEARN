package com.handler;

import com.pojo.Forum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 策略工厂
 */
@Slf4j
@Component
public class MessageHandlerFactory {
    //策略映射
    private Map<String, Supplier<MessageHandler<? extends Forum>>> supplierMap = new HashMap<>();

    public  <T extends Forum> void registerMessageHandler(String handlerStr, Supplier<? extends MessageHandler<? extends Forum>> supplier) {
        supplierMap.put(handlerStr, (Supplier<MessageHandler<? extends Forum>>) supplier);
    }

    //获取Handler对象
    public MessageHandler<? extends Forum> createMessageHandler(String handlerStr) {
        Supplier<MessageHandler<? extends Forum>> messageHandlerSupplier = supplierMap.get(handlerStr);
        if (messageHandlerSupplier != null) {
            return messageHandlerSupplier.get();
        }
        log.info("MessageHandlerFactory#createMessageHandler--未知的handlerStr：{}", handlerStr);
        return null;
    }
}