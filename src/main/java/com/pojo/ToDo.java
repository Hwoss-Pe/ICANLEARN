package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDo {
    private Integer id;
    private Integer userId;
    private String task;
    private String startTime;
    private String endTime;
    private String[][] finishArray;
    private String[][] desArray;
    private String finish;
    private String des;
    private Integer stage;
}
