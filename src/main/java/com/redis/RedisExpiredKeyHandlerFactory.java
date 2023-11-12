package com.redis;

public interface RedisExpiredKeyHandlerFactory {
     RedisExpiredKeyHandler  createHandler(Class<?> handlerClass);
}