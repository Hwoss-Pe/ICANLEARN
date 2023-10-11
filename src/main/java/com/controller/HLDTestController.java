package com.controller;

import com.pojo.HLDQuestion;
import com.pojo.HLDTestResult;
import com.pojo.Result;
import com.service.HLDService;
import com.utils.Code;
import com.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/hld")
public class HLDTestController {
    @Autowired
    private HLDService hldService;
    @Autowired
    private HttpServletRequest req;

    @GetMapping("/{num}")
    public Result getHLDQuestions(@PathVariable Integer num) {
        List<HLDQuestion> hldQuestionList = hldService.getHLDQuestionList(num);
        if (hldQuestionList == null) {
            return Result.error(Code.HLD_TEST_ERR, "获取霍兰德测试题目失败");
        }
        return Result.success(Code.HLD_TEST_OK, hldQuestionList);
    }

    @PostMapping("/report")
    public Result getHLDResult(@RequestBody Map<String, List<Integer>> data) {

        if (data.isEmpty()) {
            return Result.error(Code.HLD_REPORT_ERR, "未传入参数");
        }

        List<Integer> answer = data.get("answer");

        if (answer == null || answer.isEmpty()) {
            return Result.error(Code.HLD_REPORT_ERR, "测试结果错误");
        }

        String jwt = req.getHeader("token");

        Integer id = JwtUtils.getId(jwt);

        HLDTestResult hldTestResult = hldService.getHLDResult(id, answer);

        return Result.success(Code.HLD_REPORT_OK, hldTestResult);
    }

}
