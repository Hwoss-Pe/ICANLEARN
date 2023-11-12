package com.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RestClient {


    @Value("${spring.elasticsearch.uris}")
    private String url;
    @Bean
    RestHighLevelClient createRestClient() {
//        es的本地端口


        HttpHost host = HttpHost.create(url);
        RestClientBuilder builder = org.elasticsearch.client.RestClient.builder(host);
        return new RestHighLevelClient(builder);
    }
}
