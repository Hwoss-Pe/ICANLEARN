package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DISCReport {
    private Integer id;
    private String info;
    private String emotion;
    private String work;
    private String relation;
    private String description;
}