package com.study.usermanagement.config;

import com.study.usermanagement.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// Spring MVC 配置类：把自定义拦截器注册到需要登录的接口上
// Spring MVC 設定クラス：ログインが必要なAPIに自作インターセプターを登録する
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    // 注册拦截器：登录后才能访问的接口都需要先检查 token
    // インターセプター登録：ログイン後に使うAPIは先に token を検証する
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/me",
                        "/me/**",
                        "/admin/**",
                        "/logout",
                        "/transaction-categories",
                        "/transaction-categories/**",
                        "/transaction-records",
                        "/transaction-records/**",
                        "/transaction-stats",
                        "/transaction-stats/**",
                        "/ai",
                        "/ai/**");
    }
}
