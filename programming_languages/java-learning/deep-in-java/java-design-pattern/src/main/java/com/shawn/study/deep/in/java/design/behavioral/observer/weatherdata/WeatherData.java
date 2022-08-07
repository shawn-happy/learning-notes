package com.shawn.study.deep.in.java.design.behavioral.observer.weatherdata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shawn
 * @since 2020/7/31
 */
public class WeatherData implements Subject {
  private List<Observer> observerList = new ArrayList<>();
  private float temperature;
  private float humidity;
  private float pressure;

  @Override
  public void registerObserver(Observer observer) {
    observerList.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observerList.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observerList) {
      observer.update(temperature,humidity,pressure);
    }
  }

  public void measurementsChanged(){
    notifyObservers();
  }

  public void setMeasurements(float temperature, float humidity,float pressure){
    this.temperature = temperature;
    this.humidity = humidity;
    this.pressure = pressure;
    measurementsChanged();
  }
}
