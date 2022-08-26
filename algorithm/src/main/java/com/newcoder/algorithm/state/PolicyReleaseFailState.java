package com.newcoder.algorithm.state;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 策略发布失败
 *
 * @author fengyafei
 */
@Service
@Data
@Slf4j
public class PolicyReleaseFailState implements PolicyState {

  @Autowired private PolicyService policyService;

  @Override
  public Boolean doAction(PolicyContext policyContext, ExtraData extraData) {
    log.info("策略目前处于发布失败状态");
    policyContext.setPolicyState(this);
    policyService.updatePolicyStatus("发布失败");
    return Boolean.TRUE;
  }
}
