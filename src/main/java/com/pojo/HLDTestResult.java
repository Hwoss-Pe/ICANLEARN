package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HLDTestResult {
    private Integer id;//主键id

    private Integer userId;//用户id

    private String result;//结果
    //分数
    //R 和 A 不能同时出现：R 代表现实型 A 代表艺术型
    private Integer R = 5;
    private Integer A = 5;
    //I 和 C 不能同时出现：I 代表研究型 C 代表常规型
    private Integer I = 5;
    private Integer C = 5;
    //S 和 E 不能同时出现：S 代表社会型 E 代表企业型
    private Integer S = 5;
    private Integer E = 5;

    private List<HLDTestReport> reports;


    //计算好分数后获取最后的结果
    public List<String> calcScore(List<Integer> answer) {
        addScore(answer);

        List<String> list = new ArrayList<>();
        list.add(R > A ? "R" : "A");
        list.add(I > C ? "I" : "C");
        list.add(S > E ? "S" : "E");

        result = (R > A ? "R" : "A") + (I > C ? "I" : "C") + (S > E ? "S" : "E");

        return list;
    }


    //计算每个字母的分数
    private void addScore(List<Integer> answer) {
        for (Integer integer : answer) {
            if (integer % 6 == 1) {
                R++;
            } else if (integer % 6 == 2) {
                I++;
            } else if (integer % 6 == 3) {
                A++;
            } else if (integer % 6 == 4) {
                S++;
            } else if (integer % 6 == 5) {
                E++;
            } else {
                C++;
            }
        }
    }

}