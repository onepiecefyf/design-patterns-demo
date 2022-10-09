package com.newcoder.subject;

import java.util.ArrayList;
import java.util.List;

/**
 * 气象中转站
 *
 * @author fengyafei
 */
public class WeatherData implements Subject {

  /** 观察者列表 */
  private List<Observer> observerList;

  /** 观察的信息 温度 */
  private float temperature;
  /** 观察的信息 湿度 */
  private float humidity;
  /** 观察的信息 气压 */
  private float pressure;

  public WeatherData() {
    this.observerList = new ArrayList<>();
  }

  @Override
  public void registerObserver(Observer observer) {
    observerList.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    int i = observerList.indexOf(observer);
    if (i > 0) {
      observerList.remove(observer);
    }
  }

  @Override
  public void notifyObserver() {
    if (observerList != null && observerList.size() > 0) {
      observerList.stream()
          .forEach(
              observer -> {
                observer.update(temperature, humidity, pressure);
              });
    }
  }

  public void measurementsChanged() {
    notifyObserver();
  }

  public void setMeasurements(float temperature, float humidity, float pressure) {
    this.temperature = temperature;
    this.humidity = humidity;
    this.pressure = pressure;
    measurementsChanged();
  }
}
