package com.newcoder.algorithm.state;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 策略待发布状态
 *
 * @author fengyafei
 */
@Service
@Slf4j
@Data
public class PolicyReleaseReadyState implements PolicyState {

  @Autowired private PolicyService policyService;

  @Override
  public Boolean doAction(PolicyContext policyContext, ExtraData extraData) {
    log.info("策略目前处于待发布状态。。。。。。。");

    policyContext.setPolicyState(this);
    Boolean isSuccess = policyService.updatePolicyStatus("待发布");

    return Boolean.TRUE;
  }
}
