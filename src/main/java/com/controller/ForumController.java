package com.controller;

import com.pojo.ForumPost;
import com.pojo.ForumPostComment;
import com.pojo.ForumPostPreview;
import com.pojo.Result;
import com.service.ForumService;
import com.utils.Code;
import com.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 关于论坛的接口
 */

@Slf4j
@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;
    @Autowired
    private HttpServletRequest req;

    //发布帖子
    @PostMapping("/post")
    public Result post(@RequestBody ForumPost forumPost) {
        try {
            String jwt = req.getHeader("token");
            Integer publisherId = JwtUtils.getId(jwt);
            //设置发布者id
            forumPost.setPublisherId(publisherId);
            //上传帖子
            Integer postId = forumService.uploadPost(forumPost);
            //返回帖子的id
            return Result.success(Code.UPLOAD_POST_OK, postId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.UPLOAD_POST_ERR, "发布失败");
        }
    }


    //修改帖子开放状态(谁谁谁可见)
    @PatchMapping("/post/{postId}")
    public Result updatePost(@RequestParam Integer status, @PathVariable String postId) {
        String jwt = req.getHeader("token");
        Integer publisherId = JwtUtils.getId(jwt);

        boolean updated = forumService.updateStatus(status, publisherId, postId);

        return updated ? Result.success(Code.UPDATE_POST_STATUS_OK)
                : Result.error(Code.UPDATE_POST_STATUS_ERR, "修改帖子可见范围失败");
    }

    //删除帖子
    @DeleteMapping("/post/{postId}")
    public Result deletePost(@PathVariable String postId) {
        String jwt = req.getHeader("token");
        Integer publisherId = JwtUtils.getId(jwt);

        boolean deleted = forumService.deletePost(publisherId, postId);

        return deleted ? Result.success(Code.DELETE_POST_OK)
                : Result.error(Code.DELETE_POST_ERR, "删除帖子失败");
    }


    //预览帖子
    @GetMapping("/post/{num}")
    public Result getPosts(@PathVariable String num) {
        try {
            String jwt = req.getHeader("token");
            Integer userId = JwtUtils.getId(jwt);
            List<ForumPostPreview> list = forumService.getNumPostPreviews(userId, num);
            return Result.success(Code.PREVIEW_POST_OK, list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.PREVIEW_POST_ERR, "获取预览失败");
        }
    }

    //查看帖子详细信息
    @GetMapping("/post/details")
    public Result getPostsDetails(@RequestParam String postId) {
        try {
            String jwt = req.getHeader("token");
            Integer userId = JwtUtils.getId(jwt);
            ForumPost forumPost = forumService.getPostDetails(userId, postId);

            if (forumPost == null) {
                return Result.error(Code.VIEW_DETAILS_POST_ERR, "获取详细信息失败！");
            }

            return Result.success(Code.VIEW_DETAILS_POST_OK, forumPost);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.VIEW_DETAILS_POST_ERR, "获取信息异常！");
        }
    }

    //给帖子点赞/取消点赞
    @GetMapping("/like/{postId}")
    public Result like(@PathVariable String postId) {
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);

        //返回Integer类型数据，-1：操作失败 0：取消点赞成功 1：点赞成功
        Integer likePost = forumService.likePost(userId, postId);

        //判断返回结果
        if (likePost.equals(-1)) {
            return Result.error(Code.LIKE_POST_ERR, "操作失败，该帖子可能已经不存在");
        } else if (likePost.equals(0)) {
            return Result.success(Code.LIKE_POST_OK, "取消赞成功！");
        } else if (likePost.equals(1)) {
            return Result.success(Code.LIKE_POST_OK, "点赞成功~");
        }
        return Result.error(Code.LIKE_POST_ERR, "未知错误...");
    }

    //收藏/取消收藏帖子
    @GetMapping("/collect/{postId}")
    public Result collect(@PathVariable String postId) {
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
        //返回Integer类型数据，-1：操作失败 0：取消收藏成功 1：收藏成功
        Integer collected = forumService.collectPost(userId, postId);

        if (collected.equals(-1)) {
            return Result.error(Code.COLLECT_POST_OK, "操作失败");
        } else if (collected.equals(0)) {
            return Result.success(Code.COLLECT_POST_OK, "取消收藏成功！");
        } else if (collected.equals(1)) {
            return Result.success(Code.COLLECT_POST_OK, "收藏成功~");
        }
        return Result.error(Code.COLLECT_POST_ERR, "未知错误...");
    }

    //获取用户收藏列表的预览
    @GetMapping("/collect")
    public Result getCollectList() {
        try {
            String jwt = req.getHeader("token");
            Integer userId = JwtUtils.getId(jwt);
            //获取收藏帖子的预览
            List<ForumPostPreview> collectList = forumService.getCollectList(userId);
            return Result.success(Code.GET_COLLECTIONS_OK, collectList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.GET_COLLECTIONS_ERR, "操作失败");
        }

    }

    //评论帖子
    @PostMapping("/comment/{postId}")
    public Result comment(@PathVariable String postId, @RequestBody Map<String, String> map) {
        try {
            String jwt = req.getHeader("token");
            Integer userId = JwtUtils.getId(jwt);
            //处理数据
            ForumPostComment forumPostComment = new ForumPostComment();
            forumPostComment.setUserId(userId);
            forumPostComment.setPostId(Integer.parseInt(postId));
            //获取评论内容
            String commentContent = map.get("commentContent");
            if (map.get("parentCommentId") != null) {
                Integer parentCommentId = Integer.parseInt(map.get("parentCommentId"));
                forumPostComment.setParentCommentId(parentCommentId);
            }
            forumPostComment.setContent(commentContent);
            Integer commentId = forumService.commentPost(forumPostComment);
            //返回评论的id
            return Result.success(Code.COMMENT_POST_OK, commentId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Result.error(Code.COMMENT_POST_ERR, "评论失败");
        }
    }

    //删除评论
    @DeleteMapping("/comment/{commentId}")
    public Result deleteComment(@PathVariable String commentId) {
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
        boolean deleted = forumService.deletePostComment(userId, commentId);
        return deleted ? Result.success(Code.DELETE_COMMENT_OK)
                : Result.error(Code.DELETE_COMMENT_ERR, "删除帖子失败！");
    }

    //搜索帖子,返回帖子预览
    @GetMapping("/search")
    public Result searchPost(@RequestParam String keywords) {
        try {
            String jwt = req.getHeader("token");
            Integer userId = JwtUtils.getId(jwt);
            List<ForumPostPreview> previews = forumService.searchKeyWords(userId, keywords);

            return Result.success(Code.SEARCH_POST_OK, previews);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.SEARCH_POST_ERR, "搜索出错");
        }

    }


}
