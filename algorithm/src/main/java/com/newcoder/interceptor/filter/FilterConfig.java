package com.newcoder.interceptor.filter;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 * @author yafei.feng
 */
@Configuration
public class FilterConfig {

  @Autowired
  private LoginFilter loginFilter;

  @Autowired
  private ParameterFilter parameterFilter;


  @Bean
  public FilterRegistrationBean<LoginFilter> loginFilterFilterRegistrationBean () {

    FilterRegistrationBean<LoginFilter> loginFilterFilterRegistrationBean = new FilterRegistrationBean<>();

    loginFilterFilterRegistrationBean.setOrder(1);

    loginFilterFilterRegistrationBean.setFilter(loginFilter);

    loginFilterFilterRegistrationBean.setUrlPatterns(new ArrayList<>(Arrays.asList("/login/*")));

    return loginFilterFilterRegistrationBean;

  }

  @Bean
  public FilterRegistrationBean<ParameterFilter> parameterFilterFilterRegistrationBean () {

    FilterRegistrationBean<ParameterFilter> parameterFilterFilterRegistrationBean = new FilterRegistrationBean<>();

    parameterFilterFilterRegistrationBean.setOrder(2);

    parameterFilterFilterRegistrationBean.setFilter(parameterFilter);

    parameterFilterFilterRegistrationBean.setUrlPatterns(new ArrayList<>(Arrays.asList("/login/*")));

    return parameterFilterFilterRegistrationBean;

  }

}
