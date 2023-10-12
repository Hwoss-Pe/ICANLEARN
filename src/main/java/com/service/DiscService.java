package com.service;

import com.pojo.DiscQuestion;
import com.pojo.DiscReport;

import java.util.List;
import java.util.Map;

public interface DiscService {

    List<DiscQuestion> getQuestionLists();

    DiscReport getReport(Integer userId, Map<String,String> map);
}
