package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 帖子的评论
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumPostComment implements Forum{

    private Integer id;
    private Integer postId;//帖子id
    private Integer userId;//评论者的id
    private String content;//评论内容
    private Timestamp createTime;//创建时间
    private Integer parentCommentId;//父评论id 无则为null


    private List<ForumPostComment> subComments;//子评论

    public void add(ForumPostComment forumPostComment){
        if (subComments == null){
            subComments = new ArrayList<>();
        }
        subComments.add(forumPostComment);
    }

}
