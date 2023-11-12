package com.redis;


import com.mapper.ForumMapper;
import com.mapper.OccupationExplodeMapper;
import com.redis.handler.ForumPostLikeExpiredKeyHandler;
import com.redis.handler.Progress1ExpireKeyHandler;
import com.redis.handler.Progress2ExpireKeyHandler;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RedisKeyExpireRegister {
    @Autowired
    private ExpiredKeyHandlerFactory factory;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ForumMapper forumMapper;
    @Autowired
    OccupationExplodeMapper occupationExplodeMapper;
    @PostConstruct
    public void registerHandlers() {
        factory.registerHandler(ForumPostLikeExpiredKeyHandler.class, () -> new ForumPostLikeExpiredKeyHandler(redisUtil, forumMapper), RedisConstant.FORUM_POST_LIKE);
        factory.registerHandler(Progress1ExpireKeyHandler.class, () -> new Progress1ExpireKeyHandler(redisUtil, occupationExplodeMapper), RedisConstant.OCCUPATION_VALUES_1);
        factory.registerHandler(Progress2ExpireKeyHandler.class, () -> new Progress2ExpireKeyHandler(redisUtil, occupationExplodeMapper), RedisConstant.OCCUPATION_VALUES_2);


    }
}
