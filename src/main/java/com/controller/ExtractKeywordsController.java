package com.controller;


import com.pojo.Result;
import com.utils.Code;
import com.utils.HanLPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/extract")
public class ExtractKeywordsController {

    @Autowired
    private HanLPUtils hanLPUtils;


    //根据文字内容获取关键词
    @PostMapping("/{num}")
    public Result extractKeywords(@PathVariable Integer num, @RequestBody Map<String, String> map) {
        try {
            String text = map.get("text");
            Set<String> set = hanLPUtils.extractHighFrequencyNouns(text, num);
            return Result.success(Code.GET_RECOMMEND_KEYWORDS_OK, set);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.GET_RECOMMEND_KEYWORDS_ERR, "获取关键词失败！");
        }

    }


}