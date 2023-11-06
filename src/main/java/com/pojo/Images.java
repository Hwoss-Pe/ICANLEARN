package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Images {
    private Integer id;
    private Integer userId;
    private Integer fromId;
    private String whiteboard;
    private String fromName;
    private String characterKeywords;
    private String jobKeywords;
}