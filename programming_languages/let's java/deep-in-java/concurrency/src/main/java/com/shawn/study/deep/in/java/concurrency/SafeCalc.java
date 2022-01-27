package com.shawn.study.deep.in.java.concurrency;

// 用两个不同的锁保护value,不满足互斥关系，不能保证可见性。
public class SafeCalc {

  static long value = 0L;

  synchronized long get() {
    return value;
  }

  static synchronized void addOne() {
    value += 1;
  }
}
