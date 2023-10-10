package com.service;

import com.pojo.HLDQuestion;

import java.util.List;

public interface HLDService {
    List<HLDQuestion> getHLDQuestionList(Integer num);
}
