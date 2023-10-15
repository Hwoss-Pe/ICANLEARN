package com.config;

import com.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginCheckConfig implements WebMvcConfigurer {

    @Value("${debug.mode.enabled}")
    private boolean debugModeEnabled;

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        loginCheckInterceptor.setDebugModeEnabled(debugModeEnabled);
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").
                excludePathPatterns("/user");
    }

}
