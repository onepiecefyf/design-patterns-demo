package com.newcoder.interceptor.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 参数过滤器
 * @author yafei.feng
 *
 */
@Slf4j
@Component
public class ParameterFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String filterName = filterConfig.getFilterName();
    log.info("{}初始化", filterName);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    log.info("过滤器ParameterFilter开始对请求预处理");
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    StringBuffer requestURL = request.getRequestURL();
    log.info("请求的URL:{}", requestURL);

    filterChain.doFilter(servletRequest, servletResponse);

    log.info("ParameterFilter.doFilter处理结束");
  }

  @Override
  public void destroy() {
    log.info("销毁ParameterFilter");
  }
}
