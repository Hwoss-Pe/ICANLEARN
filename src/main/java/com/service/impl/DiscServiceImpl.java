package com.service.impl;

import com.mapper.DiscMapper;
import com.pojo.DiscQuestion;
import com.pojo.DiscReport;
import com.pojo.DiscScore;
import com.pojo.User;
import com.service.DiscService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DiscServiceImpl implements DiscService {

    @Autowired
    private DiscMapper discMapper;

    @Autowired
    private UserService userService;

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


//        将结果放入user
        User user = userService.getUserById(userId);
        user.setDisc(result);
        userService.updateUser(user);


//        根据顺序结果获取报告
        return discMapper.getDiscReport(resultId);
    }

    @Override
    public List<DiscQuestion> getQuestionLists() {
        return discMapper.getQuestionLists();
    }

    @Override
//    根据结果获取对应的id
    public Integer getResultId(String result) {
        if ("D".equals(result)) {
            return 1;
        } else if ("I".equals(result)) {
            return 2;
        } else if ("S".equals(result)) {
            return 3;
        } else if ("C".equals(result)) {
            return 4;
        }
        return null; // 如果没有匹配到结果，则返回null或者根据需求进行相应处理
    }

    @Override
    public DiscReport getDiscReport(Integer id) {
        return discMapper.getDiscReport(id);
    }
}