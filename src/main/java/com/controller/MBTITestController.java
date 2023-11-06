package com.controller;

import com.pojo.MBTIQuestion;
import com.pojo.MBTIResult;
import com.pojo.MBTITestReport;
import com.pojo.Result;
import com.service.MBTITestService;
import com.utils.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/mbti")
@RestController
public class MBTITestController {

    @Autowired
    private MBTITestService mbtiTestService;

    @Autowired
    private HttpServletRequest req;


    //根据num返回对应数目的题目
    @GetMapping("/{num}")
    public Result getMBTIQuestions(@PathVariable Integer num) {
        log.info("题目的个数:{}", num);
        try {
            List<MBTIQuestion> list = mbtiTestService.getQuestionsByQNum(num);
            return Result.success(Code.MBTI_QUESTION_OK, list);
        } catch (Exception e) {
            log.info("MBTI获取题目出错:{}", e.getMessage());
            return Result.error(Code.MBTI_QUESTION_ERR, "获取题目失败");
        }

    }


    //获取答卷返回结果
    @PostMapping
    public Result getMBTITestResult(@RequestBody Map<Integer, String> map) {
        log.info("获取答卷:{}", map);
        //获取请求头中的令牌(token)
        String jwt = req.getHeader("token");

        MBTIResult mbtiResult = mbtiTestService.getTestResult(jwt, map);

        try {
            return Result.success(Code.MBTI_RESULT_OK, mbtiResult);
        } catch (Exception e) {
            log.info("MBTI获取结果出错:{}", e.getMessage());
            return Result.error(Code.MBTI_RESULT_ERR, "获取结果出错");
        }
    }


    @GetMapping("/report/{mbti}")
    public Result getMBTITestReport(@PathVariable String mbti) {
        try {
            log.info("获取mbti报告：{}", mbti);
            MBTITestReport testReport = mbtiTestService.getTestReport(mbti);
            return Result.success(Code.MBTI_REPORT_OK, testReport);
        } catch (Exception e) {
            log.info("MBTI获取报告出错:{}", e.getMessage());
            return Result.error(Code.MBTI_REPORT_ERR, "获取报告出错");
        }

    }


}