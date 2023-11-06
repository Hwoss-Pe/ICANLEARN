package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MBTIIntro {
    private Integer id;
    private String type;
    private String intro;
    private String keywords;
}