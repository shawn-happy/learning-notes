package com.shawn.study.deep.in.java.concurrency.sdk;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

public class SemaphoreDemo<T, R> {

  final List<T> pool;

  // 用信号量实现限流器
  final Semaphore sem;

  public SemaphoreDemo(int size, T t) {
    pool = new Vector<T>();
    for (int i = 0; i < size; i++) {
      pool.add(t);
    }
    sem = new Semaphore(size);
  }

  R execute(Function<T, R> func) throws Exception {
    T t = null;
    sem.acquire();
    try {
      t = pool.remove(0);
      return func.apply(t);
    } finally {
      pool.add(t);
      sem.release();
    }
  }
}
