package com.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.pojo.Result;
import com.pojo.kaptcha;
import com.utils.Code;
import com.utils.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "图形验证码-api")
@RequestMapping("/api")
@RestController
public class CaptchaController {

    // 模拟缓存
   static Map<String,String> cache = new HashMap<>();
//   记录过期
   static Map<String, Long> expire = new HashMap<>();


    @ApiOperation("点击获取图形验证码")
    @GetMapping("/identifyImage")
    public Result identifyImage(
                              @ApiParam(value = "图形验证码id,无值：生成验证码，有值:刷新验证码")
                              @RequestParam(name = "codeId", required = false) String codeId
//                              @RequestParam(required = false)String UUID
    ) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 90, 4, 100);
        // 验证码值
        String code = lineCaptcha.getCode();
        // 模拟把验证码的值存储到缓存
        if (codeId == null) {
//            System.out.println("获取图形码");
            codeId = UUIDUtil.simpleUUID();
            // 保存图形码值
            cache.put(codeId, code);
//            这里面存的是UUID的图片对应，还有验证码
        } else {
            // 更新图形码值，此时此刻 图形码可能已经过期删除，那就相对于保存一个新的
            cache.put(codeId, code);
        }
        expire.put(codeId,  System.currentTimeMillis());

        if(codeId.equals("")){
             return Result.error(Code.VERTICAL_ERR,"验证码获取异常");
        }
        kaptcha kaptcha = new kaptcha(codeId,lineCaptcha.getImageBase64Data());
        return Result.success(Code.VERTICAL_OK,kaptcha);
    }
}