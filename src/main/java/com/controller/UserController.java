package com.controller;


import com.service.UserService;
import com.pojo.Result;
import com.pojo.User;
import com.pojo.VerticalUser;
import com.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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


//    修改登录需要传入验证码的值
    @PostMapping("/login")
    public Result login(@RequestBody VerticalUser verticaluser) {
        String UUID = verticaluser.getUuid();
        String code = verticaluser.getCode();
        User user = verticaluser.getUser();
        log.info("User: " + user.getId() + " is  logging in");

        if (!redisTemplate.hasKey(RedisConstant.CAPTCHA_CODE_ID + UUID)){
            return Result.error(Code.VERTICAL_LOGIN_ERR, "请刷新验证码");
        }
        if(Objects.isNull(code)){
            return Result.error(Code.REGISTER_ERR,"请输入验证码");
        }
        Long expireTime = (Long) redisUtil.hget(RedisConstant.CAPTCHA_CODE_ID + UUID, code);

        if (expireTime == null) {
            return Result.error(Code.REGISTER_ERR, "验证码错误");
        }

        if (expireTime < System.currentTimeMillis()) {
            return Result.error(Code.VERTICAL_LOGIN_ERR, "验证码过期");
        }
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

//    注册的时候需要传入账号和密码，账号就是邮箱，点击发送后进行验证，
    @PostMapping("/register")
    public Result register(@RequestBody VerticalUser verticalUser){
        User user = verticalUser.getUser();
        String code = verticalUser.getCode();
        log.info("User: " + user.getId()+" is  registering in");
//        这里去getAccount
//        String factCode = CaptchaController.cache.get(UUID);
        if(!isAtLeastEightCharacters(user.getPassword())) {
            return Result.error(Code.REGISTER_ERR,"密码至少8位");
        }
//        else if(!(isEmail(user.getAccount()))){
//            return Result.error(Code.REGISTER_ERR,"邮箱号格式错误");
//        }


        if(Objects.isNull(code)){
            return Result.error(Code.REGISTER_ERR,"请输入验证码");
        }

        if (!redisTemplate.hasKey(user.getAccount())){
            return Result.error(Code.REGISTER_ERR, "邮箱号发送失败");
        }

        Long expireTime = (Long) redisUtil.getExpire(user.getAccount());

        if (expireTime ==-2) {
            return Result.error(Code.REGISTER_ERR, "验证码过期");
        }
//        这里去验证code是否和redis里面的一样

        String factCode= (String) redisUtil.get(user.getAccount());
        String uuid = UUIDUtil.simpleUUID().toString().replaceAll("-", "");
        String username = uuid.substring(0, 8);
        user.setUsername(username);
        if(code.equals(factCode)){
            boolean register = userService.register(user);
            if(register){
              return  Result.success(Code.REGISTER_OK,"注册成功");
            }else {
               return Result.error(Code.REGISTER_ERR,"已存在账号");
            }
        }else {
            return Result.error(Code.VERTICAL_LOGIN_ERR, "验证码错误");
        }
    }
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody VerticalUser verticalUser) {
        User user = verticalUser.getUser();
        String password = user.getPassword();
        String account  = user.getAccount();
        String code = verticalUser.getCode();

        log.info("User: " + user.getId()+" is  resetPassword in");

        if(!isAtLeastEightCharacters(password)) {
            return Result.error(Code.LOGIN_ERR,"密码至少8位");
        }




        if(Objects.isNull(code)){
            return Result.error(Code.RESET_PASSWORD_ERR,"请输入验证码");
        }

        if (!redisTemplate.hasKey(user.getAccount())){
            return Result.error(Code.RESET_PASSWORD_ERR, "邮箱号发送失败");
        }

        Long expireTime = (Long) redisUtil.getExpire(user.getAccount());

        if (expireTime ==-2) {
            return Result.error(Code.RESET_PASSWORD_ERR, "验证码过期");
        }

        String factCode= (String) redisUtil.get(user.getAccount());
        if(!factCode.equals(code)){
            return Result.error(Code.RESET_PASSWORD_ERR,"验证码错误");
        }
        return userService.updatePassword(account, password) ?
                Result.success(Code.RESET_PASSWORD_OK)
                : Result.error(Code.RESET_PASSWORD_ERR,"账号错误");
    }




//    获取个人信息
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


    @PostMapping("/email")
    public Result sendEmail(@RequestBody Map<String,String> map) {
        String email = map.get("email");
        String code ="";
        try{
             code = EmailUtils.sendEmail(email);
            redisUtil.set(email, code,RedisConstant.CAPTCHA_EXPIRE_TIME);
        }catch (Exception e){
            return Result.error(Code.VERTICAL_ERR,"邮箱发送失败,可能找不到对方邮箱");
        }
        return Result.success(Code.VERTICAL_OK,code);
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
//    public static boolean isEmail(String email){
//            String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
//            return Pattern.matches(regex, email);
//        }

    }
