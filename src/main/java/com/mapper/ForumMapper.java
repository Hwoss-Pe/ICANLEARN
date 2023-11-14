package com.mapper;

import com.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ForumMapper {

    String POST = "posts";
    String POST_COMMENTS = "post_comments";

    //获取指定库的当前自增id的值
    @Select("select max(id) from ${tableName}")
    Integer selectIncrementId(String tableName);

    //根据帖子id查询发布者id
    @Select("select publisher_id from posts where id = #{id}")
    Integer selectPublisherIdByPostId(Integer postId);

    //新增帖子
    @Insert("insert into posts(id, title, content_words, labels, publisher_id, visible_scope,create_time)" +
            " values(#{id},#{title},#{words},#{labels},#{publisherId},#{visibleScope},#{createTime})")
    void insertForumPost(ForumPost data);

//    //新增定时发布的帖子
//    @Insert("insert into posts(id, title, content_words, labels, publisher_id, visible_scope,create_time)" +
//            " values(#{id},#{title},#{words},#{labels},#{publisherId},#{visibleScope},#{createTime}) ")
//    void insertForumPostHasCreateTime(ForumPost data);

    //根据帖子id查询帖子详细信息
    @Select("select id, title, content_words, labels, publisher_id, visible_scope, create_time, like_num, collect_num " +
            "from posts where id = #{postId}")
    ForumPost selectForumPostByPostId(String postId);

    //更改帖子的可见范围
    @Update("update posts set visible_scope = #{visibleScope} where id = #{id}")
    void updateVisibleScopeById(Integer id, Integer visibleScope);

    //根据帖子id删除帖子
    @Delete("delete from posts where id = #{id}")
    void deletePostByPostId(Integer id);

    //更新帖子表的点赞数
    @Update("update posts set like_num = like_num + #{size} where id = #{id}")
    void updateForumPostLikeNum(Integer size, Integer id);

    //新增数据到点赞表中
    void insertList2ForumPostLike(List<ForumPostLike> list);

    //根据id查询点赞列表
    @Select("select user_id from post_like where post_id = #{postId}")
    List<Integer> selectLikeUserIdByPostId(String postId);

    //删除指定postId与userId对应的点赞数据
    @Delete("delete from post_like where user_id = #{userId} and post_id = #{postId}")
    void deletePostLike(Integer userId, Integer postId);

    //根据id查询收藏列表
    @Select("select user_id from post_collections where post_id = #{postId}")
    List<Integer> selectCollectUserIdByPostId(String postId);

    //删除指定postId与userId对应的收藏数据
    @Delete("delete from post_collections where user_id = #{userId} and post_id = #{postId}")
    void deletePostCollect(Integer userId, Integer postId);

    //新增收藏表数据
    @Insert("insert into post_collections(post_id, user_id, publisher_id) VALUES (#{postId},#{userId},#{publisherId})")
    void insertForumPostCollect(ForumPostCollect data);

    //更新帖子的收藏数
    @Update("update posts set collect_num = collect_num + #{num} where id = #{postId}")
    void updateForumPostCollectNum(Integer postId, Integer num);

    //查询用户收藏的帖子表
    @Select("select p.id,p.title,p.labels,p.publisher_id,p.create_time,p.like_num,p.collect_num from post_collections pc " +
            "join posts p on pc.post_id = p.id " +
            "where pc.user_id = #{userId} and p.visible_scope != -1")
    List<ForumPostPreview> selectCollectPostPreviewsByUserId(Integer userId);

    //获取帖子对应的评论
    @Select("select id, post_id, user_id, content, create_time, parent_comment_id from post_comments where post_id = #{postId}")
    List<ForumPostComment> selectForumPostCommentsByPostId(String postId);

    //查询id对应评论的评论者id
    @Select("select user_id from post_comments where id = #{commentId}")
    Integer selectCommentUserIdByCommentId(String commentId);

    //新增评论
    @Insert("insert into post_comments(id, post_id, user_id, content, parent_comment_id) values(#{id},#{postId},#{userId},#{content},#{parentCommentId})")
    void insertForumPostComment(ForumPostComment data);

    //删除评论
    @Delete("delete from post_comments where id = #{id}")
    void deletePostCommentByCommentId(Integer id);

    //查询该用户可查看的有效帖子id
    @Select("select id from posts where publisher_id != #{userId} and visible_scope = 1 and create_time <= now()")
    List<Integer> selectForumPostIdsByUserId(Integer userId);

    //根据id列表查询帖子预览信息
    List<ForumPostPreview> selectForumPostPreViewsByIds(List<Integer> ids);


    //查询与搜索内容匹配的帖子id
    @Select("select id from posts where labels like CONCAT('%', #{keyword}, '%') or title like CONCAT('%',#{keywords},'%')")
    List<Integer> selectIdsBySearchContent(String keyWords);


    //获取帖子预览
    @Select("select p.id,p.title,p.labels,p.create_time,p.like_num,p.collect_num from post_like pl " +
            "join posts p on p.id = pl.post_id " +
            "where pl.user_id = #{userId} and p.visible_scope != -1")
    List<ForumPostPreview> selectLikePostPreviewsByUserId(Integer userId);

    //获取用户发布的帖子
    @Select("select id,title,labels,create_time,like_num,collect_num from posts where publisher_id = #{userId}")
    List<ForumPostPreview> selectPublishedPostPreViewsByUserId(Integer userId);

    //获取职业探索帖子预览
    @Select("select id,card,like_num,collect_num from occupation_person order by RAND() LIMIT #{num}")
    List<OccupationPersonPreview> selectNumOccupationPersonPreviews(Integer num);

    //获取职业探索帖子人物详细信息
    @Select("select id, card, skills, background, responsibility, quality, salary, story, challenge, evaluate, advice, prospect, like_num, collect_num" +
            " from occupation_person where id = #{id}")
    OccupationPerson selectOccupationPerson(Integer id);

    //查询点赞表
    @Select("select id from post_like where user_id=#{userId} and post_id = #{id}")
    Integer selectOccupationPersonLikeByUserId(Integer userId, Integer id);

    //加入点赞表
    @Insert("insert into post_like(user_id, post_id) values (#{userId},#{id})")
    void insertOccupationPersonLike(Integer userId, Integer id);

    //查询收藏表
    @Select("select id from post_collections where user_id = #{userId} and post_id = #{id}")
    Integer selectOccupationPersonCollectById(Integer userId, Integer id);

    //加入收藏表
    @Insert("insert into post_collections(user_id, post_id) values (#{userId},#{id})")
    void insertOccupationPersonCollect(Integer userId, Integer id);

    //点赞
    @Update("update occupation_person set like_num = like_num + #{num} where id = #{id}")
    void updateOccupationPersonLike(Integer num,Integer id);

    //收藏
    @Update("update occupation_person set collect_num = collect_num + #{num} where id = #{id}")
    void updateOccupationPersonCollect(Integer num,Integer id);


//    //根据id列表查询帖子预览
//    List<ForumPostPreview> selectPostPreviewsByIds(List<Integer> ids, Integer userId);
//    //查询帖子预览信息
//    List<ForumPostPreview> selectPostPreviewsByPostIds(List<ForumPostCollect> list);
}