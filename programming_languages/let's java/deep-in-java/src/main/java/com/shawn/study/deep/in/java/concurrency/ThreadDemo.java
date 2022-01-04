package com.shawn.study.deep.in.java.concurrency;

public class ThreadDemo {

  private static long count = 0;

  private void add() {
    int i = 0;
    while (i++ <= 10000) {
      count += 1;
    }
  }

  public static long calc() throws InterruptedException {
    final ThreadDemo test = new ThreadDemo();
    Thread t1 =
        new Thread(
            () -> {
              test.add();
            });

    Thread t2 =
        new Thread(
            () -> {
              test.add();
            });

    t1.start();
    t2.start();

    t1.join();
    t2.join();
    return count;
  }

  public static void main(String[] args) throws InterruptedException {
    // 结果为10K - 20K之间的随机数
    System.out.println(calc());
  }
}
