package com.newcoder.algorithm.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置
 */
public class WebMvcConfig implements WebMvcConfigurer {


  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(new LoginInterceptor());

    registry.addInterceptor(new OldLoginInterceptor()).addPathPatterns("admin/oldLogin");
  }

}
