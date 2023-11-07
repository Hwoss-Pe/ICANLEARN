package com.handler.forum;

import com.handler.MessageHandler;
import com.mapper.ForumMapper;
import com.pojo.ForumPostCollect;

/**
 * 取消收藏
 *
 * 删除收藏表中的数据
 * post的收藏数-1
 */
public class DeletePostCollectHandler implements MessageHandler<ForumPostCollect> {
    @Override
    public void handle(ForumPostCollect data, ForumMapper forumMapper) {
        Integer postId = data.getPostId();
        Integer userId = data.getUserId();
        forumMapper.deletePostCollect(userId, postId);
        forumMapper.updateForumPostCollectNum(postId,-1);
    }
}