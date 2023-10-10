package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String account;
    private String job;
    private String mbti;
    private String interest;
    private String interest_mbti;
    private String avatar;
    private String signature;
}
