package com.service;

import com.pojo.ForumPost;
import com.pojo.ForumPostComment;
import com.pojo.ForumPostPreview;

import java.util.List;

public interface ForumService {

    //发布帖子
    Integer uploadPost(ForumPost forumPost);

    //更新状态
    boolean updateStatus(Integer status, Integer publisherId, String postId);

    //删除帖子
    boolean deletePost(Integer publisherId, String postId);

    //获取指定数量的帖子预览
    List<ForumPostPreview> getNumPostPreviews(Integer userId, String num);

    //给对应的帖子点赞
    Integer likePost(Integer userId, String postId);

    //收藏帖子
    Integer collectPost(Integer userId, String postId);

    //获取用户的收藏表
    List<ForumPostPreview> getCollectList(Integer userId);

    //评论帖子
    Integer commentPost(ForumPostComment forumPostComment);

    //查看帖子详细信息
    ForumPost getPostDetails(Integer userId, String postId);

    //删除帖子评论
    boolean deletePostComment(Integer userId, String commentId);

    //搜索
    List<ForumPostPreview> searchKeyWords(Integer userId, String keywords);
}