package com.shawn.study.deep.in.java.concurrency.thread;

/** @author shawn */
public class ThreadExceptionDemo {

  public static void main(String[] args) throws InterruptedException {
    Thread.setDefaultUncaughtExceptionHandler(
        (thread, throwable) -> {
          System.out.printf("当前线程[%s],遇到了异常，详细信息：[%s]\n", thread.getName(), throwable.getMessage());
        });
    Thread t1 =
        new Thread(
            () -> {
              throw new RuntimeException("thread exception!");
            },
            "t1");
    t1.start();
    t1.join();
    System.out.printf("当前线程[%s]，是否存活：[%s]\n", t1.getName(), t1.isAlive());
  }
}
