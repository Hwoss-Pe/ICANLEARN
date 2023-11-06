package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FromMessage {

    private Integer FromId;
    private String Message;
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
