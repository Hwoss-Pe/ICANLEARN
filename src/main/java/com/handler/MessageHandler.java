package com.handler;

import com.mapper.ForumMapper;

/**
 * 策略接口
 * @param <T>
 */
public interface MessageHandler<T> {
    void handle(T data, ForumMapper forumMapper);
}
