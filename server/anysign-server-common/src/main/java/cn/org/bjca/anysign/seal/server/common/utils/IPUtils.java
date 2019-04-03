package cn.org.bjca.anysign.seal.server.common.utils;

import javax.servlet.http.HttpServletRequest;

/***************************************************************************
 * <pre>Ip工具类</pre>
 *
 * @文件名称: IPUtils.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/9 19:55
 ***************************************************************************/
public class IPUtils {

  public static String getIPAddress(HttpServletRequest request) {
    String ip = null;
    //X-Forwarded-For：Squid 服务代理
    String ipAddresses = request.getHeader("X-Forwarded-For");
    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown"
        .equalsIgnoreCase(ipAddresses)) {
      //Proxy-Client-IP：apache 服务代理
      ipAddresses = request.getHeader("Proxy-Client-IP");
    }
    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown"
        .equalsIgnoreCase(ipAddresses)) {
      //WL-Proxy-Client-IP：weblogic 服务代理
      ipAddresses = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown"
        .equalsIgnoreCase(ipAddresses)) {
      //HTTP_CLIENT_IP：有些代理服务器
      ipAddresses = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown"
        .equalsIgnoreCase(ipAddresses)) {
      //X-Real-IP：nginx服务代理
      ipAddresses = request.getHeader("X-Real-IP");
    }
    if (ipAddresses != null && ipAddresses.length() != 0) {
      ip = ipAddresses.split(",")[0];
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

}