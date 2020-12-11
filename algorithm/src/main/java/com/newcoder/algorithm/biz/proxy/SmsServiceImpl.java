package com.newcoder.algorithm.biz.proxy;

import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

  @Override
  public void send(String message) {
    System.out.println(message);
  }
}
