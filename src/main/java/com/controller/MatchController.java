package com.controller;


import com.pojo.MatchDegree;
import com.pojo.Result;
import com.pojo.User;
import com.service.MatchService;
import com.utils.Code;
import com.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchService matchService;
    @Autowired
    private HttpServletRequest req;

    static Map<Integer, List<User>> ListMap = new HashMap<>();

    @GetMapping("/degree")
    public Result getDegree() {
        String jwt = req.getHeader("token");
        //    根据id去获取对象的mbti，这里注意如果对象的mbti是null那么就会获取失败
        MatchDegree matchDegree = matchService.getDegree(jwt);
        if (matchDegree == null) {
            log.info("获取匹配度失败");
            return Result.error(Code.DEGREE_ERR, "获取匹配度失败");
        }


//        用fastjson会改变msg位置
//        return    JSONObject.toJSONString(Result.success(Code.DEGREE_OK,matchDegree), SerializerFeature.WriteMapNullValue);
        return Result.success(Code.DEGREE_OK, matchDegree);
    }

    @PostMapping()
    public Result match(@RequestBody(required = false) Map<String, List<String>> map, HttpSession session) {
//        这里看前端传的数据有多少个选择
        String jwt = req.getHeader("token");


        if (session.isNew()) {
            List<User> resultList = matchService.matching(jwt, map);
            session.setAttribute("resultList", resultList);
            //        这里返回的就是直接处理过后数据
            Claims claims = JwtUtils.parseJwt(jwt);
            Integer id = (Integer) claims.get("id");
//        这是类似缓存
            ListMap.put(id, resultList);
        }

        List<User> returnList = (List<User>) session.getAttribute("resultList");


        List<User> userList;
//        返回的是调整后的，每次进行这个url就要重新获取一次，点击刷新就减掉里面的
        if (returnList.size() < 5) {
            userList = new ArrayList<>(returnList);
        } else {
            userList = new ArrayList<>(returnList.subList(0, 5));
        }
        if (returnList.size() == 0) {
            log.info("get Users failed");
            return Result.error(Code.MATCH_USER_ERR, "获取用户异常");
        }
        returnList.removeAll(userList);
//        删除掉发送的数据
        return Result.success(Code.MATCH_USER_OK, userList);
    }

    @PostMapping("/1")
    public Result matchOne() {
        String jwt = req.getHeader("token");
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");
//        这里看前端传的数据有多少个选择
        List<User> userList = ListMap.get(id);
        Collections.shuffle(userList);
        User remove = userList.remove(0);
        ListMap.put(id, userList);
//        删除掉发送的数据
        return Result.success(Code.MATCH_USER_OK, remove);
    }

    @GetMapping("/match")
    public Result match(HttpSession session) {
        // 清除上一次匹配的缓存

        if (session.getAttribute("resultList") != null) {
            // 存在则进行删除
            session.removeAttribute("resultList");
        }
        return Result.success();
    }
}