package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MBTITestScore {
    private Integer id;
    private Integer userId;
    private Integer E = 0;
    private Integer I = 0;
    private Integer S = 0;
    private Integer N = 0;
    private Integer T = 0;
    private Integer F = 0;
    private Integer J = 0;
    private Integer P = 0;
    private String result;

    public void add(String type) {
        switch (type) {
            case "E" -> E++;
            case "I" -> I++;
            case "S" -> S++;
            case "N" -> N++;
            case "T" -> T++;
            case "F" -> F++;
            case "J" -> J++;
            case "P" -> P++;
        }
    }

    public String getCalcResult() {
        return (E > I ? "E" : "I") +
                (S > N ? "S" : "N") +
                (T > F ? "T" : "F") +
                (J > P ? "J" : "P");
    }
}