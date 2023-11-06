package com.service;

import com.pojo.HLDQuestion;
import com.pojo.HLDTestReport;
import com.pojo.HLDTestResult;

import java.util.List;

public interface HLDService {
    //获取霍兰德测试题目
    List<HLDQuestion> getHLDQuestionList(Integer num);

    //获取霍兰德测试结果
    HLDTestResult getHLDResult(Integer id, List<Integer> answer);

    List<HLDTestReport> getHLDTestReport(List<String> types);
}