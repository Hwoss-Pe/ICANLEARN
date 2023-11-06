package com.handler;


import com.handler.forum.*;
import com.utils.KafkaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 注册策略
 */
@Component
public class MessageHandlerRegistrar {

    @Autowired
    private MessageHandlerFactory messageHandlerFactory;


    //注册策略类映射
    @PostConstruct
    public void registerMessageHandlers() {
        messageHandlerFactory.registerMessageHandler(KafkaConstant.INSERT_POST, InsertForumPostHandler::new);
        messageHandlerFactory.registerMessageHandler(KafkaConstant.UPDATE_POST_VISIBLE_SCOPE, UpdateForumPostVisibleScopeHandler::new);
        messageHandlerFactory.registerMessageHandler(KafkaConstant.DELETE_POST, DeleteForumPostHandler::new);
        messageHandlerFactory.registerMessageHandler(KafkaConstant.DELETE_POST_LIKE, DeletePostLikeHandler::new);
        messageHandlerFactory.registerMessageHandler(KafkaConstant.DELETE_POST_COLLECT, DeletePostCollectHandler::new);
        messageHandlerFactory.registerMessageHandler(KafkaConstant.INSERT_POST_COLLECT, InsertPostCollectHandler::new);
        messageHandlerFactory.registerMessageHandler(KafkaConstant.INSERT_POST_COMMENT, InsertPostCommentHandler::new);
        messageHandlerFactory.registerMessageHandler(KafkaConstant.DELETE_POST_COMMENT, DeletePostCommentHandler::new);
    }
}