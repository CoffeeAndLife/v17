package com.hgz.v17miaosha.config;

import com.hgz.v17miaosha.interceptor.LimiterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huangguizhao
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer{

    @Autowired
    private LimiterInterceptor limiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limiterInterceptor).addPathPatterns("/**");
    }
}
