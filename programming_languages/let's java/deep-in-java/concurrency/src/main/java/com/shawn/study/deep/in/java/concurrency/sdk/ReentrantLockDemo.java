package com.shawn.study.deep.in.java.concurrency.sdk;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

  private static Lock lock = new ReentrantLock();

  public static void main(String[] args) {
    synchronizedAction(ReentrantLockDemo::action1);
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
}
