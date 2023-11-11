package com.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RestClient {
    @Bean
    public RestHighLevelClient createRestClient() {
        HttpHost host = HttpHost.create("http://8.134.211.237:9200");
        RestClientBuilder builder = org.elasticsearch.client.RestClient.builder(host);
        return new RestHighLevelClient(builder);
    }
}