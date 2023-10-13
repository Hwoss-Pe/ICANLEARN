package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscQuestion {
    private Integer id;
    private String AAnswer;
    private String BAnswer;
    private String CAnswer;
    private String DAnswer;
}
