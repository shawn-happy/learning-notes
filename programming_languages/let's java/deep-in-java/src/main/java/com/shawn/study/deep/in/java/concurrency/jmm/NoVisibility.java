package com.shawn.study.deep.in.java.concurrency.jmm;

public class NoVisibility {
  private static boolean ready;
  private static int number;

  private static class ReaderThread extends Thread {
    public void run() {
      while (!ready) {
        System.out.println("Hi");
        Thread.yield();
      }
      System.out.println(number);
    }
  }

  public static void main(String[] args) {
    new ReaderThread().start(); // 启动一个线程
    number = 42;
    ready = true;
  }
}
