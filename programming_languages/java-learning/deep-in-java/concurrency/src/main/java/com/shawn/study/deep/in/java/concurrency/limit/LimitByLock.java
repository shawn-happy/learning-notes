package com.shawn.study.deep.in.java.concurrency.limit;

public class LimitByLock {

  private int count = 10;

  public void f() {
    if (count <= 0) {
      System.out.println("拒绝执行业务逻辑");
      return;
    }

    synchronized (this) {
      if (count <= 0) {
        System.out.println("拒绝执行业务逻辑");
        return;
      }
      count--;
    }

    try {
      // 执行业务逻辑
      System.out.println("执行业务逻辑");
    } finally {
      synchronized (this) {
        count++;
      }
    }
  }

  public static void main(String[] args) {
    LimitByLock limitByLock = new LimitByLock();
    for (int i = 0; i < 100; i++) {
      Thread t = new Thread(() -> limitByLock.f());
      t.start();
    }
  }
}
