package com.handler.forum;

import com.handler.MessageHandler;
import com.mapper.ForumMapper;
import com.pojo.ForumPostComment;

public class DeletePostCommentHandler implements MessageHandler<ForumPostComment> {
    @Override
    public void handle(ForumPostComment data, ForumMapper forumMapper) {
        Integer id = data.getId();
        forumMapper.deletePostCommentByCommentId(id);
    }
}
