package com.newcoder.algorithm.biz.strategy;

import java.math.BigDecimal;
import org.springframework.beans.factory.InitializingBean;

/**
 * 普通优惠 7折优惠
 */
public class ParticularlyVipPayService implements UserPayService, InitializingBean {

  @Override
  public BigDecimal quote(BigDecimal orderPrice) {
    return orderPrice.multiply(BigDecimal.valueOf(0.7));
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // 注册响应优惠实现策略 register
    UserPayServiceStrategyFactory.register("", this);
  }
}
