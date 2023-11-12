package com;

import com.alibaba.fastjson2.JSON;
import com.listener.RequestListener;
import com.pojo.OccupationExplode;
import com.service.OccupationExplodeService;
import com.utils.JwtUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableKafka
public class DemoApplication {
    @Autowired
    private RequestListener requestListener;

    @Autowired
    private static OccupationExplodeService occupationExplodeService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(requestListener);
        return servletListenerRegistrationBean;
    }
//    @Test
//public void Test() {
//
//    Map<String, Object> map = new HashMap<String, Object>();
//    map.put("id", 1);
//    String jwt = JwtUtils.generateJwt(map);
//    System.out.println(jwt);
//}


}
