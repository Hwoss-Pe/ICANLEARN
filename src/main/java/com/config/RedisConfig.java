package com.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;

    // 自己定义了一个RedisTemplate
    @Bean
    @SuppressWarnings("all")
    //    这里的RedisConnectionFactory启动后会自动创建并且装配，在依赖导入就可以，如果不使用多个redis服务器不需要更改
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 直接使用 <String, Object>
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        // Json序列化配置
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // String 的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 创建 RedisMessageListenerContainer 实例用于监听 Redis 事件
     *
     * @param connectionFactory Redis 连接工厂
     * @return RedisMessageListenerContainer 实例
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        // 创建 RedisMessageListenerContainer 实例
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置 Redis 连接工厂
        container.setConnectionFactory(connectionFactory);


        //返回RedisMessageListenerContainer 实例
        return container;
    }

    @Bean
    RedisConnectionFactory connectionFactory() {
        // 创建 RedisStandaloneConfiguration，指定 Redis 服务器的主机和端口
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(6379);
        redisConfig.setPassword(password);

        // 创建 LettuceClientConfiguration，可以进行更高级的配置，例如连接池配置、SSL 配置等
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.defaultConfiguration();

        // 创建 LettuceConnectionFactory，将 RedisStandaloneConfiguration 和 LettuceClientConfiguration 传递进去
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

}
