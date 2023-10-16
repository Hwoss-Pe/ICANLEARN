package com.controller;


import com.pojo.MatchDegree;
import com.pojo.Result;
import com.pojo.User;
import com.service.MatchService;
import com.utils.Code;
import com.utils.JwtUtils;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchService matchService;
    @Autowired
    private HttpServletRequest req;

    private final RedisUtil redisUtil;

    @Autowired
    public MatchController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    //获取匹配度表格
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

    //匹配
    @PostMapping()
    public Result match(@RequestBody(required = false) Map<String, List<String>> map, HttpSession session) {
//        这里看前端传的数据有多少个选择
        String jwt = req.getHeader("token");
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");

        String matchKey = RedisConstant.MATCH_USER_ID + id;
        if (!map.containsKey("status")) {
            return Result.error("缺少请求状态码");
        }
        List<String> statusList = map.get("status");
        if (statusList.get(0).equals("1")) {
            //如果是第一次进行操作
            List<User> resultList = matchService.matching(id, map);
            session.setAttribute("resultList", resultList);
            //这里返回的就是直接处理过后数据
            //这是类似缓存
//            RedisUtil.sSetAndTime(matchKey,RedisConstant.MATCH_EXPIRE_TIME, resultList.toArray());
            redisUtil.lSetList(matchKey, resultList, RedisConstant.MATCH_EXPIRE_TIME);
        }

//        long size = RedisUtil.sGetSetSize(matchKey);
        long size = redisUtil.lGetListSize(matchKey);

        List<Object> userList;
        if (size < 5) {
            userList = redisUtil.lRemove(matchKey, size);
        } else {
            userList = redisUtil.lRemove(matchKey, 5);
        }
        if (size == 0) {
            log.info("get Users failed");
            return Result.error(Code.MATCH_USER_ERR, "获取用户异常");
        }
//        删除掉发送的数据
//        redisUtil.sRemove(matchKey,userList.toArray());
        return Result.success(Code.MATCH_USER_OK, userList);
    }

    //刷新匹配
    @PostMapping("/1")
    public Result matchOne() {
        String jwt = req.getHeader("token");
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");

        String matchKey = RedisConstant.MATCH_USER_ID + id;
//        这里看前端传的数据有多少个选择
        if (!redisUtil.hasKey(matchKey) || redisUtil.lGetListSize(matchKey) == 0) {
            return Result.error(Code.MATCH_USER_ERR, "获取用户异常");
        }

        List<Object> list = redisUtil.lRemove(matchKey, 1);
        User remove = (User) list.get(0);
        log.info("替换匹配用户:{}", remove);
//        删除掉发送的数据
        return Result.success(Code.MATCH_USER_OK, remove);
    }


    //清除匹配缓存
    @GetMapping("")
    public Result match(HttpSession session) {
        // 清除上一次匹配的缓存
//        这里要做的是去重新放进session里面
        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);
        String matchKey = RedisConstant.MATCH_USER_ID + id;
        if (redisUtil.hasKey(matchKey)) {

            List<User> userList = (List<User>) session.getAttribute("resultList");

            redisUtil.lSet(matchKey, userList);
        }
        log.info("清除匹配缓存");
        return Result.success();
    }
}