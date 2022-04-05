package com.shawn.study.deep.in.java.concurrency.sdk;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

  private static Lock lock = new ReentrantLock();

  public static void main(String[] args) {
    synchronizedAction(ReentrantLockDemo::action1);
    lockOpsMethods();
  }

  private static void action1() {
    System.out.println("action 1");
    synchronizedAction(ReentrantLockDemo::action2);
  }

  private static void action2() {
    System.out.println("action 2");
    synchronizedAction(ReentrantLockDemo::action3);
  }

  private static void action3() {
    System.out.println("action 3");
  }

  private static void synchronizedAction(Runnable runnable) {
    lock.lock();
    try {
      runnable.run();
    } finally {
      lock.unlock();
    }
  }

  private static void lockOpsMethods() {
    ReentrantLock lock = new ReentrantLock();
    int count = lock.getHoldCount();
    System.out.printf("在 lock() 方法调用之前的线程[%s]重进入数：%d\n", Thread.currentThread().getName(), count);
    lock(lock, 100);
  }

  private static void lock(ReentrantLock lock, int times) {

    if (times < 1) {
      return;
    }

    lock.lock();

    try {
      // times-- load, minus 1
      lock(lock, --times);
      System.out.printf(
          "第%s次在 lock() 方法调用之后的线程[%s]重进入数：%d\n",
          times + 1, Thread.currentThread().getName(), lock.getHoldCount());
    } finally {
      lock.unlock();
    }
  }
}
