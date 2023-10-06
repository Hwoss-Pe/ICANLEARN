package com.example.demo;

import com.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",1);
        String jwt = JwtUtils.generateJwt(map);
        System.out.println(jwt);
    }

}
