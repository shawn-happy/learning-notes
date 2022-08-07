package com.shawn.study.deep.in.java.design.behavioral.observer.weatherdata;

public interface Subject {

  void registerObserver(Observer observer);

  void removeObserver(Observer observer);

  void notifyObservers();
}
