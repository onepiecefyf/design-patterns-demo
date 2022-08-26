package com.newcoder.interceptor.biz.proxy;

public class SmsServiceProxy implements SmsService{

  private SmsService smsService;

  public SmsServiceProxy(SmsService smsService) {
    this.smsService = smsService;
  }

  @Override
  public void send(String message) {
    System.out.println("发送消息前");
    smsService.send(message);
    System.out.println("发送消息后");

  }
}
