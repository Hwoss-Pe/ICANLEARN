package com.pojo;

import jdk.nashorn.internal.objects.annotations.Constructor;
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
    private String disc;
    private String hld;
    private String interest_mbti;
    private String avatar;
    private String signature;
}
