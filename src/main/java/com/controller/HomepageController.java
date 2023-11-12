package com.controller;


import com.pojo.*;
import com.service.*;
import com.utils.Code;
import com.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/homepage")
public class HomepageController {
    @Autowired
    private HttpServletRequest req;

    @Autowired
    private HomePageService homePageService;
    @Autowired
    private CartoonService cartoonService;

    @Autowired OccupationExplodeService occupationExplodeService;

    @Autowired
    private RoomService roomService;
//    绘本主页展示绘本内容，实际上就是去调用绘本的接口
    @GetMapping("/cartoon")
    public Result getCartoonPage(){
        String token = req.getHeader("token");
        Integer userId = JwtUtils.getId(token);
        Cartoon cartoon = cartoonService.getCartoon(userId);
        return cartoon!=null?Result.success(Code.CARTOON_GET_OK,cartoon)
                :Result.error(Code.CARTOON_GET_ERR,"获取失败");
    }

//查看别人的绘本主页
    @GetMapping("/cartoon/{userId}")
    public Result getCartoonPage(@PathVariable Integer userId){
        Cartoon cartoon = cartoonService.getCartoon(userId);
        return cartoon!=null?Result.success(Code.CARTOON_GET_OK,cartoon)
                :Result.error(Code.CARTOON_GET_ERR,"获取失败");
    }


    @GetMapping("/report")
    public Result getReportPage(){
        String token = req.getHeader("token");
        Integer userId = JwtUtils.getId(token);
        WhiteHomepage result = homePageService.getReportPage(userId);
        return result!=null?Result.success(Code.WHITEBOARD_GET_OK,result):
                Result.error(Code.WHITEBOARD_GET_ERR,"主页内容获取失败");
    }

    @GetMapping("/report/{userId}")
    public Result getReportPage(@PathVariable Integer userId){
        WhiteHomepage result = homePageService.getReportPage(userId);
        return result!=null?Result.success(Code.WHITEBOARD_GET_OK,result):
                Result.error(Code.WHITEBOARD_GET_ERR,"主页内容获取失败");
    }

////    获取我的点赞
//    public Result getLike(){
//     occupationExplodeService.
//
//    }
////    获取我的收藏
//    public Result getCollection(){
//
//    }
}
