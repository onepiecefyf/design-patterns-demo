package com.newcoder.subject;

/**
 * 观察者模式测试类
 * @author fengyafei
 */
public class WeatherStation {
  public static void main(String[] args) {
    WeatherData weatherData = new WeatherData();
    CurrentConditionsDisplay display = new CurrentConditionsDisplay(weatherData);
    weatherData.setMeasurements(1F, 3F, 4F);
    display.display();
  }
}
