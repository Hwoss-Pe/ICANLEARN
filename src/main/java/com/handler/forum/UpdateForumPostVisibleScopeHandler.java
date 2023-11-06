package com.handler.forum;

import com.handler.MessageHandler;
import com.mapper.ForumMapper;
import com.pojo.ForumPost;

/**
 * 更新post可见范围
 */
public class UpdateForumPostVisibleScopeHandler implements MessageHandler<ForumPost> {

    @Override
    public void handle(ForumPost data, ForumMapper forumMapper) {
        Integer visibleScope = data.getVisibleScope();
        Integer id = data.getId();
        forumMapper.updateVisibleScopeById(id, visibleScope);
    }
}
