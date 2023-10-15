package com.service.impl;

import com.mapper.DISCMapper;
import com.pojo.DISCQuestion;
import com.pojo.DISCReport;
import com.pojo.DISCScore;
import com.service.DISCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DISCServiceImpl implements DISCService {
    @Autowired
    private DISCMapper discMapper;

    @Override
    public List<DISCQuestion> getQuestionLists() {
        return discMapper.getQuestionLists();
    }

    @Override
    public DISCReport getReport(Integer userId, Map<Integer, String> map) {
        Set<Map.Entry<Integer, String>> entries = map.entrySet();
        DISCScore discScore = new DISCScore();
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
}
