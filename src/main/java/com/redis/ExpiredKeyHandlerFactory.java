package com.redis;

import com.mapper.ForumMapper;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;



@Component
@Slf4j
public class ExpiredKeyHandlerFactory implements RedisExpiredKeyHandlerFactory {


    private static final Map<Class<?>, Supplier<RedisExpiredKeyHandler>> handlerRegistry = new HashMap<>();
    static  Map<String, Class<?>> map = new HashMap<>();

    public static <T extends RedisExpiredKeyHandler> void registerHandler(Class<T> handlerClass, Supplier<T> handlerSupplier,String key) {
        handlerRegistry.put(handlerClass, (Supplier<RedisExpiredKeyHandler>) handlerSupplier);
        map.put(key,handlerClass);
    }


    @Override
    public  RedisExpiredKeyHandler createHandler(Class<?> handlerClass) {
        Supplier<RedisExpiredKeyHandler> handlerSupplier = handlerRegistry.get(handlerClass);
        if (handlerSupplier == null) {
            throw new IllegalArgumentException("没有注册： " + handlerClass.getName());
        }
        return handlerSupplier.get();
    }

    Map<Class<?>, Supplier<RedisExpiredKeyHandler>> getHandlerRegistry(){
        return handlerRegistry;
    }
}