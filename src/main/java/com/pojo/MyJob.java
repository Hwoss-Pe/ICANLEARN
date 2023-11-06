package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyJob {
    private Integer id;
    private Integer userId;
    private String jobName;
    private String des;
    private Integer isPost;
    private String picture;
}