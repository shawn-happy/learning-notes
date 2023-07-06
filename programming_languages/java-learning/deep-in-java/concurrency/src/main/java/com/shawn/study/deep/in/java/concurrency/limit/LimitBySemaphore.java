package com.shawn.study.deep.in.java.concurrency.limit;

import java.util.concurrent.Semaphore;

public class LimitBySemaphore {

  private final Semaphore semaphore = new Semaphore(10);

  public void f() throws Exception {
    semaphore.acquire();
    try {
      System.out.println("执行业务逻辑");
    } finally {
      semaphore.release();
    }
  }

  public static void main(String[] args) {
    LimitBySemaphore limitBySemaphore = new LimitBySemaphore();
    for (int i = 0; i < 100; i++) {
      new Thread(
              () -> {
                try {
                  limitBySemaphore.f();
                } catch (Exception e) {
                }
              })
          .start();
    }
  }
}
