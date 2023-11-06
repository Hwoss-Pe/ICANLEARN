package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendSettings {
    private  Integer id;
    private Integer userId;
    private String content;
    private Integer counts;
}