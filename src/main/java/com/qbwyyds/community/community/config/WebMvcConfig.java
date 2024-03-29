package com.qbwyyds.community.community.config;

import com.qbwyyds.community.community.annotation.LoginRequire;
import com.qbwyyds.community.community.controller.interceptor.Alphainterceptor;
import com.qbwyyds.community.community.controller.interceptor.LoginInterceptor;
import com.qbwyyds.community.community.controller.interceptor.LoginRequiredInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private Alphainterceptor alphainterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphainterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.html","/**/*.jpg","/**/*.png","/**/*.jpeg")
                .addPathPatterns("/site/login","/site/register");
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.html","/**/*.jpg","/**/*.png","/**/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.html","/**/*.jpg","/**/*.png","/**/*.jpeg");
    }
}
