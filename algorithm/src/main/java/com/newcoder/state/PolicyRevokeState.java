package com.newcoder.state;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 策略撤销状态
 * @author fengyafei
 */
@Service
@Data
@Slf4j
public class PolicyRevokeState implements PolicyState {

  @Autowired private PolicyService policyService;

  @Override
  public Boolean doAction(PolicyContext policyContext, ExtraData extraData) {
    log.info("开始执行策略撤销动作");
    Policy policyInfo = policyService.getPolicyInfo(extraData.getPolicyId());

    // 核验当前策略状态 只有成功或者失败状态 可以选择撤销
    String status = policyInfo.getStatus();
    if (StringUtils.equals(status, "发布成功") || StringUtils.equals(status, "发布失败")) {
      log.info("执行撤销操作成功，更新策略状态为待发布");
      policyContext.setPolicyState(new PolicyReleaseReadyState());
      policyContext.updatePolicyStatus("待发布", extraData);
    } else {
      log.info("非法操作");
    }

    // todo 开始执行其他操作 比如：和策略相关表的CRUD操作

    return Boolean.TRUE;
  }
}
