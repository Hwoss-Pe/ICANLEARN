package com.controller;

import com.pojo.HLDQuestion;
import com.pojo.Result;
import com.service.HLDService;
import com.utils.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/hld")
public class HLDTestController {
    @Autowired
    private HLDService hldService;
    @GetMapping("/{num}")
    public Result getHLDQuestions(@PathVariable Integer num){
        List<HLDQuestion> hldQuestionList = hldService.getHLDQuestionList(num);
        if(hldQuestionList==null){
            return Result.error(Code.HLD_TEST_ERR,"获取霍兰德测试题目失败");
        }
        return Result.success(Code.HLD_TEST_OK,hldQuestionList);
    }
}
