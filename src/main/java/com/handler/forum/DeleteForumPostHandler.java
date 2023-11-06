package com.handler.forum;

import com.handler.MessageHandler;
import com.mapper.ForumMapper;
import com.pojo.ForumPost;

/**
 * 删除post
 * 根据postId删除帖子
 */
public class DeleteForumPostHandler implements MessageHandler<ForumPost> {
    @Override
    public void handle(ForumPost data, ForumMapper forumMapper) {
        forumMapper.deletePostByPostId(data.getId());
    }
}
