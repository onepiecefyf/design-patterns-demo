package com.newcoder.interceptor.biz.proxy;

public class SmsMain {

  public static void main(String[] args) {

    SmsService smsService = new SmsServiceImpl();

    SmsServiceProxy smsServiceProxy = new SmsServiceProxy(smsService);

    smsServiceProxy.send("开始发送消息吧");

  }

}
