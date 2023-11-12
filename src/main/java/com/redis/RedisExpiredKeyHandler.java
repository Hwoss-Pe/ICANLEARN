package com.redis;

public interface  RedisExpiredKeyHandler {
    void handleExpiredKey(String expiredKey);
}