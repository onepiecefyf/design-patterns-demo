package com.newcoder.interceptor.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
