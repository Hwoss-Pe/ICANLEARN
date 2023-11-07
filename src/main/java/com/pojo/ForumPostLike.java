package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 帖子点赞表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumPostLike implements Forum{

    private Integer id;//自增主键
    private Integer userId;//点赞人id
    private Integer postId;//帖子id
    private Timestamp createTime;//点赞的时间

}