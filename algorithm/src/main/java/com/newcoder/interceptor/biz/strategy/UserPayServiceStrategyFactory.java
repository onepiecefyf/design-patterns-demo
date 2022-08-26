package com.newcoder.interceptor.biz.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略工厂类
 */
public class UserPayServiceStrategyFactory {

  private static Map<String, UserPayService> services = new ConcurrentHashMap<>();

  public static UserPayService getUserByType(String type) {
    return services.get(type);
  }

  public static void register(String userType, UserPayService userPayService) {
    services.put(userType, userPayService);
  }

}
