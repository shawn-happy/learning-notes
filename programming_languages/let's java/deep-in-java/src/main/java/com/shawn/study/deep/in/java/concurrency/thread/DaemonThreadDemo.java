package com.shawn.study.deep.in.java.concurrency.thread;

/**
 * 守护线程demo
 *
 * @author shawn
 */
public class DaemonThreadDemo {

  public static void main(String[] args) {
    Thread t1 =
        new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                System.out.println("Hello World!" + i);
              }
            },
            "t1");
    t1.setDaemon(true);
    t1.start();
  }
}
