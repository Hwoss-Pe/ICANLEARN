package com.handler.forum;

import com.handler.MessageHandler;
import com.mapper.ForumMapper;
import com.pojo.ForumPost;

/**
 * 新增post
 * ForumPost
 */
public class InsertForumPostHandler implements MessageHandler<ForumPost> {
    @Override
    public void handle(ForumPost data, ForumMapper forumMapper) {
        forumMapper.insertForumPost(data);
    }
}