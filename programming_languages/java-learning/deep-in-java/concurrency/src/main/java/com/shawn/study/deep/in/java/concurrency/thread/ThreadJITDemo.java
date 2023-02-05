package com.shawn.study.deep.in.java.concurrency.thread;

public class ThreadJITDemo {

  private static boolean running = true;
  private static int count = 0;

  public static void main(String[] args) throws Exception {
    Thread t1 =
        new Thread(
            () -> {
              while (running) {
                count++;
              }
              System.out.println("count = " + count);
            });

    Thread t2 =
        new Thread(
            () -> {
              running = false;
            });

    t1.start();
    Thread.sleep(1000);
    t2.start();
    t2.join();
    t1.join();
  }
}
