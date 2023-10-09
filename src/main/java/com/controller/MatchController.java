package com.controller;


import com.service.MatchService;
import com.pojo.MatchDegree;
import com.pojo.Result;
import com.pojo.User;
import com.utils.Code;
import com.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        if(matchDegree==null){
            log.info("获取匹配度失败");
            return   Result.error(Code.DEGREE_ERR,"获取匹配度失败");
        }


//        用fastjson会改变msg位置
//        return    JSONObject.toJSONString(Result.success(Code.DEGREE_OK,matchDegree), SerializerFeature.WriteMapNullValue);
        return   Result.success(Code.DEGREE_OK,matchDegree);
    }
    @PostMapping()
    public Result match(@RequestBody(required = false) Map<String, List<String>> map, HttpSession session) {
//        这里看前端传的数据有多少个选择
        String jwt = req.getHeader("token");
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");
        if (!map.containsKey("status")){
            return Result.error("缺少请求状态码");
        }
            List<String> statusList = map.get("status");
            if(statusList.get(0).equals("1")){
//                如果是第一次进行操作
                List<User> resultList = matchService.matching(jwt, map);
                session.setAttribute("resultList", resultList);
                //        这里返回的就是直接处理过后数据
//        这是类似缓存
                ListMap.put(id, resultList);
            }
        List<User> returnList = ListMap.get(id);

        List<User> userList = new ArrayList<>();
//        返回的是调整后的，每次进行这个url就要重新获取一次，点击刷新就减掉里面的
        if (returnList.size()<5){
            userList = new ArrayList<>(returnList);
        }else {
            userList = new ArrayList<>(returnList.subList(0,5));
        }
        if (returnList.size()==0) {
            log.info("get Users failed");
            return Result.error(Code.MATCH_USER_ERR, "获取用户异常");

        }
        returnList.removeAll(userList);
        ListMap.put(id,returnList);
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
        ListMap.put(id,userList);
//        删除掉发送的数据
        return Result.success(Code.MATCH_USER_OK, remove);
    }

    @GetMapping()
    public Result match(HttpSession session) {
        // 清除上一次匹配的缓存
//        这里要做的是去重新放进session里面
        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);
        if (ListMap.containsKey(id)) {

            List<User> userList =(List<User>) session.getAttribute("resultList");

            ListMap.put(id,userList);
        }


        return Result.success();
    }
}