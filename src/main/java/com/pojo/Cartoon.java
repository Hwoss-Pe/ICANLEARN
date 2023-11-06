package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cartoon {
    private Integer id;
    private Integer userId;
    private String myName;
    private String birth;
    private String[] ad;
    private String[] disAd;
    private String preMe;
    private String postMe;
    private String area;
    private String userJob;
    private List<MyJob> myJobList;


}