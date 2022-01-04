package com.shawn.study.deep.in.java.concurrency.thread;

/**
 * 线程状态
 *
 * @author shawn
 */
public class ThreadStatusDemo {

  public static void main(String[] args) throws Exception{
    Thread t = new Thread(ThreadStatusDemo::sayHi);
    System.out.printf("before start Thread: Thread Status: [%s]\n", t.getState());
    t.start();
    System.out.printf("before join Thread: Thread Status: [%s]\n", t.getState());
    t.join();
    System.out.printf("thread finish: Thread Status: [%s]\n", t.getState());
  }

  public static void sayHi() {
    System.out.println("Hello World");
    System.out.printf(
        "execute sayHi Method, Thread Status: [%s]\n", Thread.currentThread().getState());
  }
}
