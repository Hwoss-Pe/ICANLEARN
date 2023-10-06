package com.controller;


import com.service.RadioWaveService;
import com.pojo.Result;
import com.pojo.User;
import com.service.RadioWaveService;
import com.utils.Code;
import com.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/wave")
public class RadioWaveController {

    @Autowired
    private RadioWaveService radioWaveService;
    @Autowired
    private HttpServletRequest req;

    @GetMapping("/send/{sendId}")
    public Result sendWave(@PathVariable Integer sendId) {

        try {
            //获取请求头中的令牌(token)
            String jwt = req.getHeader("token");
            //获取用户id
            Claims claims = JwtUtils.parseJwt(jwt);
            Integer id = (Integer) claims.get("id");
            //处理发送电波业务
            radioWaveService.sendWave(id, sendId);

            return Result.success(Code.WAVE_SEND_OK);
        } catch (Exception e) {
            return Result.error(Code.WAVE_SEND_ERR, "电波发送失败！");
        }
    }

    @GetMapping("/accept/{addId}")
    public Result addFriend(@PathVariable Integer addId) {

        try {
            //获取请求头中的令牌(token)
            String jwt = req.getHeader("token");
            //获取用户id
            Claims claims = JwtUtils.parseJwt(jwt);
            Integer id = (Integer) claims.get("id");
            radioWaveService.acceptWave(id, addId);

            return Result.success(Code.WAVE_ACCEPT_OK);
        } catch (Exception e) {
            return Result.error(Code.WAVE_ACCEPT_ERR,"接受电波失败");
        }

    }

    @GetMapping("/query/wait")
    public Result queryWaitingList(){

        //获取请求头中的令牌(token)
        String jwt = req.getHeader("token");
        //获取用户id
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");
        List<User> list = radioWaveService.queryWaitingList(id);

        if (list == null){
            return Result.error(Code.QUERY_WAITING_ERR,"等待列表为空");
        }
        return Result.success(Code.QUERY_WAITING_OK,list);

    }

    @GetMapping("/query/friends")
    public Result queryFriendsList(){
        //获取请求头中的令牌(token)
        String jwt = req.getHeader("token");
        //获取用户id
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");
        List<User> list = radioWaveService.queryFriendsList(id);

        if (list == null){
            return Result.error(Code.QUERY_FRIEND_ERR,"好友列表为空");
        }
        return Result.success(Code.QUERY_FRIEND_OK,list);
    }


}