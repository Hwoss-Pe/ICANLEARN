package com.controller;

import com.service.UserService;
import com.pojo.Result;
import com.utils.Code;
import com.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/avatar")
class HandleAvatarUploadController {
    @Autowired
    private UserService userService;
    @Autowired
    HttpServletRequest req;
    @PostMapping("/upload")
    public Result handleAvatarUpload(@RequestBody  Map<String,String> map) {
        // 获取Base64编码格式的头像字符串
        Integer user_id = Integer.parseInt(map.get("user_id"));
        String avatar64 =  map.get("avatar");
        //     获取到64后直接存入数据库
        if(userService.updateAvatar64(user_id, avatar64)){
            return Result.success(Code.UPLOAD_AVATAR_OK);
        }
        return Result.error(Code.UPLOAD_AVATAR_ERR,"头像上传失败");
    }
//    获取对应用户的头像,如果传userId就是获取别人的，如果不传就是默认获取自己的
    @GetMapping("/{num}")
    public Result getAvatar(  @PathVariable(required = false) Integer num){
        Integer userId;
        if(num ==0){
//            获取自己的头像
            String token = req.getHeader("token");
             userId = JwtUtils.getId(token);
        }else {
//            获取指定id的头像
            userId=num;
        }
        String avatar = userService.getAvatar(userId);
        return avatar!=null?Result.success(Code.GET_AVATAR_OK,avatar)
                :Result.error(Code.GET_AVATAR_ERR,"用户没头像");
    }
}
