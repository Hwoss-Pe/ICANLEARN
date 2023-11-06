package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscScore {
    private Integer id;
    private Integer userId;
    private Integer D = 0;
    private Integer I = 0;
    private Integer S = 0;
    private Integer C = 0;
    private String result;



    public void increment(String answer){
        switch (answer) {
            case "A" -> D++;
            case "B" -> I++;
            case "C" -> S++;
            case "D" -> C++;
        }
    }

    public String getResult() {
        int max = Math.max(Math.max(D, I), Math.max(S, C));
        if (max == D) {
            result = "D";
        } else if (max == I) {
            result = "I";
        } else if (max == S) {
            result = "S";
        } else {
            result = "C";
        }
        return result;
    }
    public Integer getResultId() {
        if ("D".equals(result)) {
            return 1;
        } else if ("I".equals(result)) {
            return 2;
        } else if ("S".equals(result)) {
            return 3;
        } else if ("C".equals(result)) {
            return 4;
        }
        return null; // 如果没有匹配到结果，则返回null或者根据需求进行相应处理
    }

}
