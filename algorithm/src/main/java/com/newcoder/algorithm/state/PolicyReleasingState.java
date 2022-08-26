package com.newcoder.algorithm.state;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发布中状态
 *
 * @author fengyafei
 */
@Service
@Data
@Slf4j
public class PolicyReleasingState implements PolicyState {

  @Autowired private PolicyService policyService;

  @Override
  public Boolean doAction(PolicyContext policyContext, ExtraData extraData) {
    System.out.println("策略发布中。。。。。。");

    // 1、 首先核验策略状态，只有处于待发布状态，才可以发布
    Policy policyInfo = policyService.getPolicyInfo(extraData.getPolicyId());
    String status = policyInfo.getStatus();
    if (StringUtils.equals(status, "待发布")) {
      // 2、 修改策略状态为发布中
      policyContext.setPolicyState(this);
      Boolean releaseSuccess = policyService.updatePolicyStatus("发布中");

      // 3、 发布成功，修改策略状态为成功，发布失败，修改策略状态为失败
      if (releaseSuccess) {
        policyContext.setPolicyState(new PolicyReleaseSuccessState());
        policyContext.updatePolicyStatus("发布成功", extraData);
      } else {
        policyContext.setPolicyState(new PolicyReleaseFailState());
        policyContext.updatePolicyStatus("发布失败", extraData);
      }
    } else {
      log.info("非法操作");
    }
    return Boolean.TRUE;
  }
}
