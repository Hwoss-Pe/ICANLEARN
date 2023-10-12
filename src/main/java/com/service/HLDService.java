package com.service;

import com.pojo.HLDQuestion;
import com.pojo.HLDTestResult;

import java.util.List;

public interface HLDService {
    List<HLDQuestion> getHLDQuestionList(Integer num);

    //获取霍兰德测试结果
    HLDTestResult getHLDResult(Integer id, List<Integer> answer);
}
