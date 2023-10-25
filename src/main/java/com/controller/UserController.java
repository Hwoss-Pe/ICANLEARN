package com.controller;


import com.service.UserService;
import com.pojo.Result;
import com.pojo.User;
import com.pojo.VerticalUser;
import com.utils.Code;
import com.utils.JwtUtils;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest req;

    @Autowired
    private RedisTemplate redisTemplate;


    private final RedisUtil redisUtil;

    @Autowired
    public UserController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("User: " + user.getId() + " is  logging in");

        User login = userService.login(user);

        if (login != null){
            Map<String,Object> map = new HashMap<>();
            map.put("id",login.getId());
            map.put("account",login.getAccount());
            String jwt = JwtUtils.generateJwt(map);
            return Result.success(Code.LOGIN_OK,jwt);
        }

        return Result.error(Code.LOGIN_ERR, "登录失败");

    }
    @PostMapping("/register")
    public Result register(@RequestBody VerticalUser verticaluser){
        String UUID = verticaluser.getUuid();
        String code = verticaluser.getCode();
        User user = verticaluser.getUser();
        log.info("UUID: {} , code:{} ,user:{}",UUID,code,user);
        log.info("User: " + user.getId()+" is  registering in");
//        这里去getAccount
//        String factCode = CaptchaController.cache.get(UUID);



        if (!redisTemplate.hasKey(RedisConstant.CAPTCHA_CODE_ID + UUID)){
            return Result.error(Code.VERTICAL_LOGIN_ERR, "请刷新验证码");
        }
        if(code==null){
            return Result.error(Code.REGISTER_ERR,"请输入验证码");
        }

        Long expireTime = (Long) redisUtil.hget(RedisConstant.CAPTCHA_CODE_ID + UUID, code);
//        Long oldTime = CaptchaController.expire.get(UUID);
//        if(oldTime==null){
//                oldTime = System.currentTimeMillis();
//        }

        long newTime  = System.currentTimeMillis();
//        前端响应提示
//        if(newTime-oldTime>1200000){
//            return Result.error(Code.VERTICAL_LOGIN_ERR,"验证码过期");
//        }
//
//        else if(!(factCode.equals(code))){
//            return Result.error(Code.VERTICAL_LOGIN_ERR,"请刷新验证码");
//        }

        if (expireTime == null) {
            return Result.error(Code.REGISTER_ERR, "验证码错误");
        }

        if (expireTime < System.currentTimeMillis()) {
            return Result.error(Code.VERTICAL_LOGIN_ERR, "验证码过期");
        }
        if(!isAtLeastEightCharacters(user.getPassword())) {
            return Result.error(Code.REGISTER_ERR,"密码至少8位");
        }
        else if(!(isElevenDigits(user.getAccount()))){
            return Result.error(Code.REGISTER_ERR,"手机号格式错误");
        }

        String uuid = UUID.toString().replaceAll("-", "");
        String username = uuid.substring(0, 8);
        user.setUsername(username);
        return userService.register(user) ? Result.success(Code.REGISTER_OK) :
                Result.error(Code.REGISTER_ERR,"已存在账号");

    }
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody User user) {
        String password = user.getPassword();
        String account  = user.getAccount();
        log.info("User: " + user.getId()+" is  resetPassword in");

        if(!isAtLeastEightCharacters(password)) {
            return Result.error(Code.LOGIN_ERR,"密码至少8位");
        }

        return userService.updatePassword(account, password) ?
                Result.success(Code.RESET_PASSWORD_OK)
                : Result.error(Code.RESET_PASSWORD_ERR,"账号错误");

    }
    @GetMapping("{user_id}")
    public Result getUserById(@PathVariable  Integer user_id){
        User user  = userService.getUserById(user_id);
        log.info("get user" + user_id);

        if(user == null){
            log.info("查询不到对应的用户");
            return Result.error(Code.FIND_USER_ERR,"查询不到对应的用户");

        }
        return Result.success(Code.FIND_USER_OK,user);
    }
    @PutMapping()
    public Result updateUser(@RequestBody User user){
        String jwt = req.getHeader("token");
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer user_id = (Integer) claims.get("id");

        user.setId(user_id);
//        大写处理
        user.setMbti(user.getMbti().toUpperCase());

        user.setInterest_mbti(  user.getInterest_mbti().toUpperCase());
        return userService.updateUser(user) ? Result.success(Code.USER_UPDATE_OK): Result.error(Code.USER_UPDATE_ERR,"更新失败");
    }
    public boolean isElevenDigits(String str) {
        // 使用正则表达式匹配11位数字
        String regex = "^\\d{11}$";
        return Pattern.matches(regex, str);
    }
    public static boolean isAtLeastEightCharacters(String str) {
        // 使用正则表达式匹配至少8位字符
        String regex = "^.{8,}$";
        return Pattern.matches(regex, str);
    }
}