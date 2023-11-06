package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Integer toId;
    private String message;
    private Integer x;
    private Integer y;


    //    颜色#000000
    private String color;
    //    笔的宽度012
    private Integer thick;
    //    画图id
    private Integer canvasId;
    private String finish;
    private String firstDraw;
}