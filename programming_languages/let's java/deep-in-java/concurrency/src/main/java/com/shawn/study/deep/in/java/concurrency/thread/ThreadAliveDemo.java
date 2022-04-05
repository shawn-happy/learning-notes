package com.shawn.study.deep.in.java.concurrency.thread;

/**
 * 线程状态demo
 *
 * @author shawn
 */
public class ThreadAliveDemo {

  public static void main(String[] args) {
    Thread t1 =
        new Thread(
            () -> System.out.printf("当前线程[%s], 正在执行...\n", Thread.currentThread().getName()), "t1");

    t1.start();
    System.out.printf("当前线程[%s], 是否存活[%s]\n", t1.getName(), t1.isAlive());
  }
}
