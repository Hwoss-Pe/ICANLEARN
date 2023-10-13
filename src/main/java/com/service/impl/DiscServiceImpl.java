package com.service.impl;

import com.mapper.DiscMapper;
import com.pojo.DiscQuestion;
import com.pojo.DiscReport;
import com.pojo.DiscScore;
import com.service.DiscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DiscServiceImpl implements DiscService {

    @Autowired
    private DiscMapper discMapper;

    @Override
    public DiscReport getReport(Integer userId, Map<Integer, String> map) {
        Set<Map.Entry<Integer, String>> entries = map.entrySet();
        DiscScore discScore = new DiscScore();
        for (Map.Entry<Integer, String> entry : entries) {
            discScore.setUserId(userId);
            String value = entry.getValue().toUpperCase();
            discScore.increment(value);
        }
        String result = discScore.getResult();
//        插入分数表里面
        if(discMapper.isExistUserId(userId)>=1){
            discMapper.updateDiscScore(discScore);
        }else {
            discMapper.setDiscScore(discScore);
        }
        Integer resultId = discScore.getResultId();

//        根据顺序结果获取报告
        return discMapper.getDiscReport(resultId);
    }

    @Override
    public List<DiscQuestion> getQuestionLists() {
        return discMapper.getQuestionLists();
    }
}
