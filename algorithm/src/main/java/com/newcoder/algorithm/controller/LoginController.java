package com.newcoder.algorithm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 * @author yafei.feng
 *
 */

@RestController
@RequestMapping("/login")
public class LoginController {

  @RequestMapping("/hello")
  public String login() throws Exception{

    Thread.sleep(1000L);

    return "hello!";
  }



}
