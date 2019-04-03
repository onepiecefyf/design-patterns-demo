package cn.org.bjca.anysign.seal.server.common.config;

import cn.org.bjca.anysign.seal.server.common.interceptor.HttpParamInterceptor;
import cn.org.bjca.anysign.seal.server.common.interceptor.TraceIdInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/***************************************************************************
 * <pre>服务配置</pre>
 *
 * @author july_whj
 * @文件名称: AppConfigurer.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.config
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/5 19:34
 ***************************************************************************/
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

  @Bean
  public HandlerInterceptor getInterceptor() {
    return new HttpParamInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
    registry.addInterceptor(getInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**",
            "/swagger-ui.html/**", "/configuration/**");
    registry.addInterceptor(new TraceIdInterceptor());
    super.addInterceptors(registry);
  }

}