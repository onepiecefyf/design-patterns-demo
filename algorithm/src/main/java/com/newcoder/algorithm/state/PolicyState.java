package com.newcoder.algorithm.state;

/**
 * 状态接口
 *
 * @author fengyafei
 */
public interface PolicyState {

  /**
   * 具体业务实现方法
   * @param policyContext 策略上下文
   * @return
   */
  Boolean doAction(PolicyContext policyContext, ExtraData extraData);
}
