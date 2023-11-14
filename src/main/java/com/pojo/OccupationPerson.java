package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupationPerson {
    private Integer id;
    private String card;
    private String skills;
    private String background;
    private String responsibility;
    private String quality;
    private String salary;
    private String story;
    private String challenge;
    private String evaluate;
    private String advice;
    private String prospect;
}