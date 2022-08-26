package com.newcoder.algorithm.state;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 策略删除
 *
 * @author fengyafei
 */
@Service
@Data
@Slf4j
public class PolicyDeleteState implements PolicyState {

  @Autowired PolicyService policyService;

  @Override
  public Boolean doAction(PolicyContext policyContext, ExtraData extraData) {
    System.out.println("策略删除中。。。。。。");

    // 1、 首先核验策略状态，只有处于待发布、发布成功、发布失败才可以删除
    Policy policyInfo = policyService.getPolicyInfo(extraData.getPolicyId());
    String status = policyInfo.getStatus();
    if (StringUtils.equals(status, "待发布")
        || StringUtils.equals(status, "发布成功")
        || StringUtils.equals(status, "发布失败")) {
      // 2、 修改策略状态为删除
      policyContext.setPolicyState(this);
      policyService.updatePolicyStatus("删除");
      // todo 操作和策略相关表的数据
    } else {
      log.info("非法操作");
    }
    return Boolean.TRUE;
  }
}
