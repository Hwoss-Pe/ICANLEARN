package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class kaptcha {
    private String UUID;
    private String img;
}
