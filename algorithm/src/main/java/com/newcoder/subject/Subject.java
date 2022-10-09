package com.newcoder.subject;

/**
 * 主题接口
 * @author fengyafei
 */
public interface Subject {

  /**
   * 注册观察者
   */
  void registerObserver(Observer observer);

  /**
   * 删除观察者
   */
  void removeObserver(Observer observer);

  /**
   * 通知观察者
   */
  void notifyObserver();

}
