package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 帖子收藏表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumPostCollect implements Forum{
    private Integer id;
    private Integer postId;
    private Integer userId;
    private Integer publisherId;
    private Timestamp createTime;
}
