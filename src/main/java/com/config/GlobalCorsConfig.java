package com.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由，这里如果修改可能出现bug，springboot目前的注解CrossOrigin失效问题未找到，排除版本错误
                registry.addMapping("/**")
                // 允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 允许任意证书
                .allowCredentials(true)
                // 允许的方法
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(36000);
    }
}
