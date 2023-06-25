package com.shawn.study.deep.in.java.concurrency.waitnotify;

public interface Queue {

  void put(int e) throws InterruptedException;

  int take() throws InterruptedException;

  int size();

  boolean isEmpty();

}
