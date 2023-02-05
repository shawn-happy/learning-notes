package com.shawn.study.deep.in.java.concurrency.thread;

public class SynchronizedDemo {

  private int v;
  private static int a;
  private final Object lock = new Object();

  public synchronized void add(int value) {
    v += value;
  }

  public void sub(int value) {
    synchronized (lock) {
      v -= value;
    }
  }

  public static synchronized void multi(int value) {
    a *= value;
  }

  public static void div(int value) {
    synchronized (SynchronizedDemo.class) {
      a /= value;
    }
  }
}
