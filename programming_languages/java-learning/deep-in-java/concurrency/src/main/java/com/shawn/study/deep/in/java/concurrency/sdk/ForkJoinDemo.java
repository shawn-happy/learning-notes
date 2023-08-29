package com.shawn.study.deep.in.java.concurrency.sdk;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo {

  public static void main(String[] args) {
    ForkJoinPool pool = ForkJoinPool.commonPool();
    Long result = pool.invoke(new Fibonacci(30));
    System.out.println(result);
    System.out.println(fibonacci(30));
  }

  public static long fibonacci(long num) {
    if (num <= 1) return num;
    else return fibonacci(num - 1) + fibonacci(num - 2);
  }

  static class Fibonacci extends RecursiveTask<Long> {

    private final long num;

    Fibonacci(long num) {
      this.num = num;
    }

    public long getNum() {
      return num;
    }

    @Override
    protected Long compute() {
      if (num <= 1) return num;
      Fibonacci f1 = new Fibonacci(num - 1);
      // 创建子任务
      f1.fork();
      Fibonacci f2 = new Fibonacci(num - 2);
      // 等待子任务结果，并合并结果
      return f2.compute() + f1.join();
    }
  }
}
