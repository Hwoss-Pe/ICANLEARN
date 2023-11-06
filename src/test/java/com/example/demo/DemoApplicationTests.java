package com.example.demo;

import com.alibaba.fastjson2.JSON;
import com.config.WebSocketConfig;
import com.pojo.OccupationExplode;
import com.service.OccupationExplodeService;
import com.utils.JwtUtils;
import com.utils.UUIDUtil;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ComponentScan(basePackages = "com.service")
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

    private RestHighLevelClient client;

    @Autowired
    private static OccupationExplodeService occupationExplodeService;
    @Test
    void getName(){
        redisTemplate.opsForValue().set("a","nihao!");
        System.out.println(redisTemplate.opsForValue().get("a"));
    }


    @BeforeEach
        //在测试类中每个操作运行前运行的方法
    void setUp() {
        HttpHost host = HttpHost.create("http://localhost:9200");
        RestClientBuilder builder = RestClient.builder(host);
        client = new RestHighLevelClient(builder);
    }

    @AfterEach
         //在测试类中每个操作运行后运行的方法
    void tearDown() throws IOException {
        client.close();
    }



    @Test
    void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("book");
        client.indices().create(request, RequestOptions.DEFAULT);
    }



//单个
    @Test
    public  void addOne() throws IOException {
        List<OccupationExplode> occupations = occupationExplodeService.getOccupations();
        IndexRequest request = new IndexRequest("occupation_explode").id("1");
        OccupationExplode test = occupations.get(0);
        request.source(JSON.toJSONString(test), RequestOptions.DEFAULT);
        client.index(request, RequestOptions.DEFAULT);
    }
}
