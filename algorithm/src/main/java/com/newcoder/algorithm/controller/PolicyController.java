package com.newcoder.algorithm.controller;

import com.newcoder.algorithm.state.ExtraData;
import com.newcoder.algorithm.state.PolicyContext;
import com.newcoder.algorithm.state.PolicyDeleteState;
import com.newcoder.algorithm.state.PolicyReleasingState;
import com.newcoder.algorithm.state.PolicyRevokeState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/policy")
public class PolicyController {

  @RequestMapping("/release")
  @GetMapping
  public String release(ExtraData extraData) throws Exception {
    PolicyContext policyContext = new PolicyContext();
    policyContext.setPolicyState(new PolicyReleasingState());
    policyContext.updatePolicyStatus("发布", extraData);
    return "hello!";
  }

  @RequestMapping("/revoke")
  @GetMapping
  public String revoke(ExtraData extraData) throws Exception {
    PolicyContext policyContext = new PolicyContext();
    policyContext.setPolicyState(new PolicyRevokeState());
    policyContext.updatePolicyStatus("撤销", extraData);
    return "hello!";
  }

  @RequestMapping("/delete")
  @GetMapping
  public String delete(ExtraData extraData) throws Exception {
    PolicyContext policyContext = new PolicyContext();
    policyContext.setPolicyState(new PolicyDeleteState());
    policyContext.updatePolicyStatus("删除", extraData);
    return "hello!";
  }
}
