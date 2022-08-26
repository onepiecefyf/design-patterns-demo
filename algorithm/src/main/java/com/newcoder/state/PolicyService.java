package com.newcoder.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 策略相关呢业务
 *
 * @author fengyafei
 */
@Service
@Slf4j
public class PolicyService {

  /**
   * 修改策略状态
   * @param status 状态
   * @return
   */
  public Boolean updatePolicyStatus(String status) {
    log.info("修改策略状态为{}中。。。。。。", status);
    return Boolean.TRUE;
  }

  /**
   * 获得策略详情
   * @param id 状态
   * @return
   */
  public Policy getPolicyInfo(String id) {
    log.info("获得策略详情{}。。。。。。", id);
    return null;
  }

}
