package com.service;

import com.pojo.*;

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

    //获取用户的点赞表
    List<ForumPostPreview> getLikeList(Integer userId);

    //评论帖子
    Integer commentPost(ForumPostComment forumPostComment);

    //查看帖子详细信息
    ForumPost getPostDetails(Integer userId, String postId);

    //删除帖子评论
    boolean deletePostComment(Integer userId, String commentId);

    //搜索
    List<ForumPostPreview> searchKeyWords(Integer userId, String keywords);

    //获取用户发布过的帖子
    List<ForumPostPreview> getPublishedPosts(Integer userId);

    //获取职业探索人物帖子预览
    List<OccupationPersonPreview> getOccupationPersonPreviews(Integer num);

    //获取职业探索帖子人物详细信息
    OccupationPerson getOccupationPersonDetail(Integer id);

    //点赞职业探索帖子人物
    Integer likeOccupationPerson(Integer userId, Integer id);

    //收藏职业探索帖子人物
    Integer collectOccupationPerson(Integer userId, Integer id);

    //获取id对应帖子的评论
    List<ForumPostComment> getCommentsById(String id);

    //获取信息
    List<ForumPostMessage> getMessage(Integer userId);

    //获取未读信息数
    Integer getMessageNum(Integer userId);
}
