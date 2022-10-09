package com.newcoder.subject;

/**
 * 布告板 实现观察者
 *
 * @author fengyafei
 */
public class CurrentConditionsDisplay implements Observer {

  private WeatherData weatherData;
  private float temperature;
  private float humidity;
  private float pressure;

  /***
   * 构造器 注册观察者
   * @param weatherData
   */
  public CurrentConditionsDisplay(WeatherData weatherData) {
    this.weatherData = weatherData;
    weatherData.registerObserver(this);
  }

  @Override
  public void update(float temperature, float humidity, float pressure) {
    this.temperature = temperature;
    this.humidity = humidity;
  }

  public void display() {
    System.out.println(temperature + "-" + humidity + "-" + pressure);
  }
}
