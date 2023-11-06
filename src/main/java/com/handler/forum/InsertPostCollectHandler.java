package com.handler.forum;

import com.handler.MessageHandler;
import com.mapper.ForumMapper;
import com.pojo.ForumPostCollect;
import lombok.extern.slf4j.Slf4j;

/**
 * 收藏post
 * 新增ForumPostCollect
 * post收藏数+1
 */
@Slf4j
public class InsertPostCollectHandler implements MessageHandler<ForumPostCollect> {
    @Override
    public void handle(ForumPostCollect data, ForumMapper forumMapper) {
        log.info("InsertPostCollectHandler执行");
        Integer postId = data.getPostId();
        //获取发布者id
        Integer publisherId = forumMapper.selectPublisherIdByPostId(postId);
        data.setPublisherId(publisherId);
        //存入数据库
        forumMapper.insertForumPostCollect(data);
        //更新post的收藏数
        forumMapper.updateForumPostCollectNum(postId,1);
        log.info("数据存入收藏表:{}", data);
    }
}
