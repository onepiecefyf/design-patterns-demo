package com.newcoder.state;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发布上下文
 *
 * @author fengyafei
 */
@Data
@NoArgsConstructor
public class PolicyContext {

  private PolicyState policyState;

  public Boolean updatePolicyStatus(String status, ExtraData extraData) {
    policyState.doAction(this, extraData);
    return Boolean.TRUE;
  }
}
