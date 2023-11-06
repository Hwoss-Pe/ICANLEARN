package com.service.impl;

import com.mapper.HLDMapper;
import com.pojo.HLDQuestion;
import com.pojo.HLDTestReport;
import com.pojo.HLDTestResult;
import com.pojo.User;
import com.service.HLDService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HLDServiceImpl implements HLDService {
    @Autowired
    private HLDMapper hldMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<HLDQuestion> getHLDQuestionList(Integer num) {
        List<HLDQuestion> questionList = hldMapper.getQuestionList();
        return new ArrayList<>(questionList.subList(0,num));
    }


    @Override
    public HLDTestResult getHLDResult(Integer id, List<Integer> answer) {

        //定义Score对象
        HLDTestResult hldTestResult = new HLDTestResult();
        //根据答案计算结果，返回类型的list
        List<String> list = hldTestResult.calcScore(answer);
        //将id赋值给score对象的user_id变量
        hldTestResult.setUserId(id);
        //将答题情况存入数据库
        hldMapper.insertHLDTestScore(hldTestResult);
        //获取答题结果对应的报告
        List<HLDTestReport> reports = hldMapper.getHLDTestReport(list);

        hldTestResult.setReports(reports);





//        增加将结果存入用户的hld
        User user = userService.getUserById(id);
        user.setHld(hldTestResult.getResult());
        userService.updateUser(user);

        return hldTestResult;
    }

    @Override
    public List<HLDTestReport> getHLDTestReport(List<String> types) {
        return hldMapper.getHLDTestReport(types);
    }
}