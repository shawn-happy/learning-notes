package com.shawn.study.deep.in.java.concurrency.limit;

import java.util.concurrent.atomic.AtomicInteger;

public class LimitByAtomic {

  private static final AtomicInteger COUNTER = new AtomicInteger(10);

  public void f() {
    int count = COUNTER.decrementAndGet();
    if (count < 0) {
      COUNTER.incrementAndGet();
      System.out.println("拒绝执行业务逻辑");
      return; // 拒绝执行业务逻辑
    }

    try {
      // 执行业务逻辑
      System.out.println("执行业务逻辑");
    } finally {
      COUNTER.incrementAndGet();
    }
  }

  public static void main(String[] args) {
    LimitByAtomic limitByAtomic = new LimitByAtomic();
    for (int i = 0; i < 100; i++) {
      Thread t = new Thread(() -> limitByAtomic.f());
      t.start();
    }
  }
}
