package cn.org.bjca.anysign.seal.server.common.interceptor;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

/**
 * @author: lvyong6
 */
@Slf4j
public class TraceIdInterceptor extends HandlerInterceptorAdapter {

  public static final String TRACE_ID = "__trace_id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String traceId = StringUtils.defaultIfBlank(request.getHeader(TRACE_ID), StringUtils
        .defaultIfBlank(WebUtils.findParameterValue(request, TRACE_ID),
            TraceIdInterceptor.getUUID()));
    try {
      MDC.put(TRACE_ID, traceId);
    } catch (Exception e) {
      log.error("[mdc_error]", e);
    }
    LoggerFactory.getLogger(getClass()).info("__trace_id :{}", traceId);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex)
      throws Exception {
    try {
      MDC.remove(TRACE_ID);
    } catch (Exception e) {
      log.error("[mdc_error]", e);
    }
  }

  public static String getUUID() {
    String uuid = UUID.randomUUID().toString();
    return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid
        .substring(19, 23)
        + uuid.substring(24);
  }
}
