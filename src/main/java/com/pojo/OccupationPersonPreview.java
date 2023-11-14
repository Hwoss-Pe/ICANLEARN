package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupationPersonPreview {
    private Integer id;
    private String card;
    private String title;
    private Integer likeNum;
    private Integer collectNum;

    public void setCard(String card) {
        this.card = card;
        String pattern1 = "((现任|现为).+?)[，。,；]";
        Matcher matcher1 = Pattern.compile(pattern1).matcher(card);
        if (matcher1.find()){
            this.title = matcher1.group(1).replaceAll("(现任|现为)","");
            return;
        }

        String pattern3 = "(毕业于)([^，。；,]+)";
        Matcher matcher3 = Pattern.compile(pattern3).matcher(card);
        if (matcher3.find()) {
            this.title = matcher3.group(2) + "毕业生";
        }else {
            this.title = card;
        }
    }
}