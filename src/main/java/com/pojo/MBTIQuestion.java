package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MBTIQuestion {
    private Integer id;
    private String content;
    private String AAnswer;
    private String BAnswer;
    private String AType;
    private String BType;
    private String type;
}