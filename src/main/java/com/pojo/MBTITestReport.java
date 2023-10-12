package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MBTITestReport {
    private String mbtiName;//mbti
    private String abbreviation;//简称
    private String briefIntroduction;//简介
    private String analysis;//基础分析
    private String advantage;//性格优点
    private String disadvantage;//性格缺点
    private String careerReference;//职业参考
    private String undergraduateProgram;//本科专业参考
    private String postSecondaryProgram;//专科专业参考
    private String bookList;//书单推荐
    private String songList;//歌单推荐
    private String movieList;//电影推荐

    //四种人格的介绍
    private String EITypeIntroduction;
    private String SNTypeIntroduction;
    private String TFTypeIntroduction;
    private String JPTypeIntroduction;
}