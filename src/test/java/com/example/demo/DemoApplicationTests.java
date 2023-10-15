package com.example.demo;

import com.config.WebSocketConfig;
import com.utils.JwtUtils;
import com.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

class DemoApplicationTests {

//    @Test
//    void contextLoads() {
//
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("id",5);
//        String jwt = JwtUtils.generateJwt(map);
//        System.out.println(jwt);
//    }
    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    @Test
    void getName(){
        redisTemplate.opsForValue().set("a","nihao!");
        System.out.println(redisTemplate.opsForValue().get("a"));
    }

}
