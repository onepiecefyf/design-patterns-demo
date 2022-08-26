package com.newcoder.interceptor.controller;

import com.newcoder.interceptor.entity.MsgTrace;
import com.newcoder.interceptor.service.MsgTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获得短信信息
 * @author yafei.feng
 */
@RestController
@RequestMapping("/msg")
public class MsgTraceController {

  @Autowired
  private MsgTraceService msgTraceService;

  @RequestMapping("/trace")
  public MsgTrace getMsgTraceByTraceId() {
    return msgTraceService.getMsgTraceByTraceId("1");
  }

}
