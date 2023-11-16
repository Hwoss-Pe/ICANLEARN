package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumPostMessage {

    private Integer id;
    private Integer userId;
    private String username;
    private Integer postId;
    private Integer publisherId;
    private String title;
    private String type;

}