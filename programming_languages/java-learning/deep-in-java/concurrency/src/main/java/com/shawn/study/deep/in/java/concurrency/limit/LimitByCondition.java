package com.shawn.study.deep.in.java.concurrency.limit;

public class LimitByCondition {

  private int count = 10;

  public void f() throws Exception {
    synchronized (this) {
      while (count <= 0) {
        System.out.println("等待执行业务逻辑");
        this.wait();
      }
      count--;
    }

    try {
      System.out.println("执行业务逻辑");
    } finally {
      synchronized (this) {
        count++;
        this.notifyAll();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    LimitByCondition limitByCondition = new LimitByCondition();
    for (int i = 0; i < 100; i++) {
      Thread t =
          new Thread(
              () -> {
                try {
                  limitByCondition.f();
                } catch (Exception e) {
                }
              });
      t.start();
    }
  }
}
