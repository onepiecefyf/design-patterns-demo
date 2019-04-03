package cn.org.bjca.anysign.seal.server.common.interceptor;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.server.common.metric.MetricsNamePoint;
import cn.org.bjca.anysign.seal.server.common.utils.IPUtils;
import cn.org.bjca.anysign.seal.server.common.wrapper.BodyReaderHttpServletRequestWrapper;
import cn.org.bjca.footstone.metrics.client.metrics.MetricsClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/***************************************************************************
 * <pre>参数拦截器</pre>
 *
 * @author july_whj
 * @文件名称: HttpParamInterceptor.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.Interceptor
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/5 19:16
 ***************************************************************************/
@Component
@Slf4j
public class HttpParamInterceptor implements HandlerInterceptor {


  private static final String POST = "POST";
  private static final String NOT_INTERCEPTOR_URL = "/error";
  private static final String HEAD_URL = "/head";
  private static final ThreadLocal<MetricsClient> METRICS = new ThreadLocal<>();
  private static final ThreadLocal<Boolean> NEED_METRICS = new ThreadLocal<>();

  @Override
  public boolean preHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpSrvletResponse, Object o) throws Exception {
    NEED_METRICS.set(false);
    if (getUrl(httpServletRequest).contains(HEAD_URL)) {
      return true;
    }
    Assert.notEntity(getAppId(httpServletRequest), StatusConstants.APP_ID, null, null);
    log.info(
        "The IP {} , in time {}, access version is {} , deviceId is {} , appId is {} , url is {}",
        IPUtils.getIPAddress(httpServletRequest), System.currentTimeMillis(),
        httpServletRequest.getParameter("version"),
        httpServletRequest.getParameter("deviceId"), getAppId(httpServletRequest),
        getUrl(httpServletRequest)
    );
    MetricsNamePoint metricsNamePoint = new MetricsNamePoint(getUrl(httpServletRequest));
    if (StringUtils.isNotEmpty(metricsNamePoint.getSecondName()) && StringUtils
        .isNotEmpty(metricsNamePoint.getThirdName())) {
      METRICS.set(MetricsClient
          .newInstance(metricsNamePoint.getFirstName(), metricsNamePoint.getSecondName(),
              metricsNamePoint.getThirdName()));
      NEED_METRICS.set(true);
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o,
      ModelAndView modelAndView) throws Exception {
    if (NEED_METRICS.get()) {
      METRICS.get().sr_incrSuccess();
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o,
      Exception e) throws Exception {
    if (NEED_METRICS.get()) {
      METRICS.get().qps().rt().sr_incrTotal();
    }
    METRICS.remove();
    NEED_METRICS.remove();
  }

  /**
   * 获取请求url
   */
  private String getUrl(HttpServletRequest request) {
    return request.getRequestURL().toString();
  }

  /**
   * 获取get,post请求中appId
   *
   * @param httpServletRequest 请求信息
   */
  private String getAppId(HttpServletRequest httpServletRequest) {
    String appId;
    if (getUrl(httpServletRequest).endsWith(NOT_INTERCEPTOR_URL)) {
      //处理系统内部自请求赋值appId
      return "default";
    }
    if (POST.equals(httpServletRequest.getMethod())) {
      JSONObject parameterMap = JSON.parseObject(
          new BodyReaderHttpServletRequestWrapper(httpServletRequest)
              .getBodyString(httpServletRequest));
      appId = (String) parameterMap.get("appId");
    } else {
      appId = httpServletRequest.getParameter("appId");
    }
    return appId;
  }


}