package com.controller;

import com.pojo.DiscQuestion;
import com.pojo.DiscReport;
import com.pojo.Result;
import com.service.DiscService;
import com.utils.Code;
import com.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("/disc")
@RestController
@Slf4j
public class DiscTestController {
    @Autowired
    private DiscService discService;

    @Autowired
    private HttpServletRequest req;

    @GetMapping()
    public Result getDiscQuestions(){
        List<DiscQuestion> discQuestions = discService.getQuestionLists();

        return discQuestions==null?Result.error(Code.DISC_TEST_ERR,"获取disc题目出错"):
                Result.success(Code.DISC_TEST_OK,discQuestions);
    }
    @PostMapping("/report")
    public Result getDiscReport(@RequestBody Map<String, String> listMap){

        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);

        DiscReport report = discService.getReport(id, listMap);
        if(report==null){
            return Result.error(Code.DISC_REPORT_ERR,"获取Disc测试结果错误");
        }
        return Result.success(Code.DISC_REPORT_OK,report);
    }
}
