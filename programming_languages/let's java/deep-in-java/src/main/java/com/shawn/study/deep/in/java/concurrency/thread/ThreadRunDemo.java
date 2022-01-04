package com.shawn.study.deep.in.java.concurrency.thread;

/**
 * 线程运行的demo
 *
 * @author shawn
 */
public class ThreadRunDemo {

  public static void main(String[] args) {
    Thread t1 = new MyThread("t1");
    Thread t2 = new Thread(new MyRunnableImpl(), "t2");
    t1.start();
    t2.start();
  }

  private static void action() {
    System.out.printf("当前线程[%s], 正在执行。。。\n", Thread.currentThread().getName());
  }

  private static class MyThread extends Thread {

    public MyThread(String name) {
      super(name);
    }

    @Override
    public void run() {
      action();
    }
  }

  /** 可以使用lambda表达 */
  private static class MyRunnableImpl implements Runnable {
    @Override
    public void run() {
      action();
    }
  }
}
