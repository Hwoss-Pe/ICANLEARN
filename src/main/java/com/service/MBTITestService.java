package com.service;

import com.pojo.MBTIResult;
import com.pojo.MBTIQuestion;
import com.pojo.MBTITestReport;

import java.util.List;
import java.util.Map;

public interface MBTITestService {

    List<MBTIQuestion> getQuestionsByQNum(Integer type);
    //返回测试结果
    MBTIResult getTestResult(String jwt, Map<Integer, String> map);

    //获取MBTI测试报告
    MBTITestReport getTestReport(String mbti);
}