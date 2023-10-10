package com.service.impl;

import com.mapper.HLDMapper;
import com.pojo.HLDQuestion;
import com.service.HLDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HLDServiceImpl implements HLDService {
    @Autowired
    private HLDMapper hldMapper;

    @Override
    public List<HLDQuestion> getHLDQuestionList(Integer num) {
        List<HLDQuestion> questionList = hldMapper.getQuestionList();
        List<HLDQuestion> returnList = new ArrayList<>(questionList.subList(0,num));
        return returnList;
    }
}
