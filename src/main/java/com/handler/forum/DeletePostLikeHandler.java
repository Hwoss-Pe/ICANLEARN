package com.handler.forum;

import com.handler.MessageHandler;
import com.mapper.ForumMapper;
import com.pojo.ForumPostLike;

/**
 * 取消点赞
 * 删除点赞表中的数据
 * post点赞数-1
 */
public class DeletePostLikeHandler implements MessageHandler<ForumPostLike> {
    @Override
    public void handle(ForumPostLike data, ForumMapper forumMapper) {
        Integer postId = data.getPostId();
        Integer userId = data.getUserId();
        forumMapper.deletePostLike(userId, postId);
        forumMapper.deleteMessageByPostIdAndUserId(postId,userId);
        forumMapper.updateForumPostLikeNum(postId,-1);
    }
}
