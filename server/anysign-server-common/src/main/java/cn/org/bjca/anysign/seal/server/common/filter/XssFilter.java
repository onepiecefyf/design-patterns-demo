package cn.org.bjca.anysign.seal.server.common.filter;

import cn.org.bjca.anysign.seal.server.common.wrapper.BodyReaderHttpServletRequestWrapper;
import cn.org.bjca.anysign.seal.server.common.wrapper.XssHttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***************************************************************************
 * <pre>XssFilter</pre>
 *
 * @文件名称: XssFilter.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.filter
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/6 17:51
 ***************************************************************************/
public class XssFilter implements Filter {

  private static Logger logger = LoggerFactory.getLogger(XssFilter.class);
  /**
   * 是否过滤富文本内容
   */
  private static boolean IS_INCLUDE_RICH_TEXT = false;

  public List<String> excludes = new ArrayList<>();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    if (logger.isDebugEnabled()) {
      logger.debug("xss filter is open");
    }
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    if (handleExcludeURL(req, resp)) {
      filterChain.doFilter(request, response);
      return;
    }
    HttpServletRequest httpServletRequest = new BodyReaderHttpServletRequestWrapper(
        (HttpServletRequest) request);
    XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(httpServletRequest,
        IS_INCLUDE_RICH_TEXT);
    filterChain.doFilter(xssRequest, response);
  }

  private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {

    if (excludes == null || excludes.isEmpty()) {
      return false;
    }

    String url = request.getServletPath();
    for (String pattern : excludes) {
      Pattern p = Pattern.compile("^" + pattern);
      Matcher m = p.matcher(url);
      if (m.find()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    if (logger.isDebugEnabled()) {
      logger.debug("xss filter init.....");
    }
    String isIncludeRichText = filterConfig.getInitParameter("isIncludeRichText");
    if (StringUtils.isNotBlank(isIncludeRichText)) {
      IS_INCLUDE_RICH_TEXT = BooleanUtils.toBoolean(isIncludeRichText);
    }
    String temp = filterConfig.getInitParameter("excludes");
    if (temp != null) {
      String[] url = temp.split(",");
      for (int i = 0; url != null && i < url.length; i++) {
        excludes.add(url[i]);
      }
    }
  }

  @Override
  public void destroy() {
  }

}