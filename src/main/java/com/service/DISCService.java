package com.service;

import com.pojo.DISCQuestion;
import com.pojo.DISCReport;

import java.util.List;
import java.util.Map;

public interface DISCService {

    List<DISCQuestion> getQuestionLists();

    DISCReport getReport(Integer userId, Map<Integer, String> map);
}
