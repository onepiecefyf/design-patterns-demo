package com.newcoder.algorithm.biz.strategy;

import java.math.BigDecimal;

/**
 * 策略类入口接口
 */
public interface UserPayService {

  /**
   * 计算应付价格
   * @param orderPrice
   * @return
   */
  BigDecimal quote(BigDecimal orderPrice);

}
