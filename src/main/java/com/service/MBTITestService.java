package com.service;

import com.pojo.MBTIResult;
import com.pojo.Question;
import com.pojo.TestReport;

import java.util.List;
import java.util.Map;

public interface MBTITestService {

    List<Question> getQuestionsByQNum(Integer type);
    //返回测试结果
    MBTIResult getTestResult(String jwt, Map<Integer, String> map);

    //获取MBTI测试报告
    TestReport getTestReport(String mbti);
}