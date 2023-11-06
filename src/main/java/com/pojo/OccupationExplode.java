package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupationExplode {
    private Integer id;
    private String info;
    private String professionalEthics;
    private String duties;
    private String environment;
    private String knowledgeBg;
    private String course;
    private String skills;
    private String requirements;
    private String prospect;
    private String salary;
    private String commonTools;
    private String job;
    private Integer likes;
    private Integer collection;
    private List<String> suggestion;
//    这里就需要info和job的内容加入到里面

    @Override
    public String toString() {
        return "OccupationExplode{" +
                "id=" + id +
                ", info='" + info + '\'' +
                ", professionalEthics='" + professionalEthics + '\'' +
                ", duties='" + duties + '\'' +
                ", environment='" + environment + '\'' +
                ", knowledgeBg='" + knowledgeBg + '\'' +
                ", course='" + course + '\'' +
                ", skills='" + skills + '\'' +
                ", requirements='" + requirements + '\'' +
                ", prospect='" + prospect + '\'' +
                ", salary='" + salary + '\'' +
                ", commonTools='" + commonTools + '\'' +
                ", job='" + job + '\'' +
                '}';
    }


//    获取后记得调用
    public void setSuggestion() {
        this.suggestion = Arrays.asList(this.job,this.info);
    }
}
