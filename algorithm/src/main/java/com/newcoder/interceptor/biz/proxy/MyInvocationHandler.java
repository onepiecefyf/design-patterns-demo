package com.newcoder.interceptor.biz.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {

  private final Object object;

  public MyInvocationHandler(Object object) {
    this.object = object;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    System.out.println("执行方法前");
    Object invoke = method.invoke(object, args);
    System.out.println("执行方法后");
    return invoke;
  }
}
