package com.shawn.study.deep.in.java.concurrency.thread;

/**
 * 线程创建Demo
 *
 * @author shawn
 */
public class ThreadCreatingDemo {

  public static void main(String[] args) {
    Thread t1 =
        new Thread(
            () -> {
              action();
            },
            "t1");
  }

  private static void action() {
    System.out.printf("当前线程[%s], 正在执行。。。\n", Thread.currentThread().getName());
  }
}
