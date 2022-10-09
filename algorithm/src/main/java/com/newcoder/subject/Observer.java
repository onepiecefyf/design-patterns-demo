package com.newcoder.subject;

/**
 * 观察者接口
 *
 * @author fengyafei
 */
public interface Observer {

  /**
   * 观察的信息 方法
   *
   * @param temperature
   * @param humidity
   * @param pressure
   */
  void update(float temperature, float humidity, float pressure);
}
