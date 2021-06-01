package com.shawn.design.behavioral.observer.weatherdata;

/**
 * @author shawn
 * @since 2020/7/31
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {
  private float temperature;
  private float humidity;
  private Subject subject;

  public CurrentConditionsDisplay(Subject subject) {
    this.subject = subject;
    this.subject.registerObserver(this);
  }

  @Override
  public void display() {
    System.out.printf(
        "Current Conditions: temperature: %f F degree and humidity: %f\n", temperature, humidity);
  }

  @Override
  public void update(float temp, float humidity, float pressure) {
    this.temperature = temp;
    this.humidity = humidity;
    display();
  }
}
