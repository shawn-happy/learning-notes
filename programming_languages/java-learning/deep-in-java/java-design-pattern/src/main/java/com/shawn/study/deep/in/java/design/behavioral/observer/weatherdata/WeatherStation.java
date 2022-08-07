package com.shawn.study.deep.in.java.design.behavioral.observer.weatherdata;

/**
 * @author shawn
 * @since 2020/7/31
 */
public class WeatherStation {

  public static void main(String[] args) {
    Subject subject = new WeatherData();
    CurrentConditionsDisplay display = new CurrentConditionsDisplay(subject);
    StatisticsDisplay statisticsDisplay = new StatisticsDisplay(subject);
    ForecastDisplay forecastDisplay = new ForecastDisplay(subject);
    WeatherData weatherData = WeatherData.class.cast(subject);
    weatherData.setMeasurements(80, 65, 30.4f);
    weatherData.setMeasurements(82, 70, 29.2f);
    weatherData.setMeasurements(78, 90, 29.2f);
  }
}
