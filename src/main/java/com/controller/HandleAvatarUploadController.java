package com.controller;

import com.service.UserService;
import com.pojo.Result;
import com.utils.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping()
class HandleAvatarUploadController {
    @Autowired
    private UserService userService;
    @PostMapping("/upload-avatar")
    public Result handleAvatarUpload(@RequestBody  Map<String,String> map) {
        // 获取Base64编码格式的头像字符串
        Integer user_id = Integer.parseInt(map.get("user_id"));
        String avatar64 =  map.get("avatar64");
        //     获取到64后直接存入数据库
        if(userService.updateAvatar64(user_id, avatar64)){
            return Result.success(Code.UPLOAD_AVATAR_OK);
        }
        return Result.error(Code.UPLOAD_AVATAR_ERR,"头像上传失败");
    }
}
