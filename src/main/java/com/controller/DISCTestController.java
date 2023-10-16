package com.controller;

import com.pojo.DISCQuestion;
import com.pojo.DISCReport;
import com.pojo.Result;
import com.service.DISCService;
import com.utils.Code;
import com.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/disc")
public class DISCTestController {
    @Autowired
    private DISCService discService;

    @Autowired
    private HttpServletRequest req;
    @GetMapping
    public Result getDiscQuestions(){
        List<DISCQuestion> discQuestions = discService.getQuestionLists();

        return discQuestions==null?Result.error(Code.DISC_TEST_ERR,"获取disc题目出错"):
                Result.success(Code.DISC_TEST_OK,discQuestions);
    }
    @PostMapping("/report")
    public Result getDiscReport(@RequestBody Map<Integer, String> listMap){

        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);

        DISCReport report = discService.getReport(id, listMap);
        if(report==null){
            return Result.error(Code.DISC_REPORT_ERR,"获取Disc测试结果错误");
        }
        return Result.success(Code.DISC_REPORT_OK,report);
    }
}
