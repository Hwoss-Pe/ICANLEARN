package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Kaptcha {
    private String UUID;
    private String img;
}
