package com.shawn.study.deep.in.java.design.behavioral.observer.weatherdata;

public interface Observer {

  void update(float temp, float humidity, float pressure);
}
