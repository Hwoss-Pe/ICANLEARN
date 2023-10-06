package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestScore {
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
            case "E":
                E++;
                break;
            case "I":
                I++;
                break;
            case "S":
                S++;
                break;
            case "N":
                N++;
                break;
            case "T":
                T++;
                break;
            case "F":
                F++;
                break;
            case "J":
                J++;
                break;
            case "P":
                P++;
                break;
        }
    }

    public String getCalcResult() {
        return (E > I ? "E" : "I") +
                (S > N ? "S" : "N") +
                (T > F ? "T" : "F") +
                (J > P ? "J" : "P");
    }
}