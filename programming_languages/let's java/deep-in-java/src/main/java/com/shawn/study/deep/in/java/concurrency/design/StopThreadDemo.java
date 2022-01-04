package com.shawn.study.deep.in.java.concurrency.design;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StopThreadDemo {

  public static void main(String[] args) throws Exception {
    StopInterface stop = new StopMethod();
    // stop.stop();
    stop = new InterruptMethod();
    stop.stop();
  }
}

interface StopInterface {

  void stop() throws Exception;
}

class StopMethod implements StopInterface {

  @Override
  public void stop() throws Exception {
    CountThread count = new CountThread();
    count.start();
    TimeUnit.SECONDS.sleep(1);
    count.stop();
  }

  class CountThread extends Thread {

    @Override
    public void run() {
      for (int i = 0; i < 2 * Math.pow(10, 6); i++) {
        System.out.println(i);
      }
    }
  }
}

class InterruptMethod implements StopInterface {

  @Override
  public void stop() throws Exception {
    CountThread count = new CountThread();
    count.start();
    TimeUnit.SECONDS.sleep(1);
    count.interrupt();
  }

  class CountThread extends Thread {

    @Override
    public void run() {
      for (int i = 0; i < 2 * Math.pow(10, 6); i++) {
        if (!currentThread().isInterrupted()) {
          System.out.println(i);
        }
      }
    }
  }
}

class BestPractice extends Thread {
  private volatile boolean finished = false; // ① volatile条件变量

  public void stopMe() {
    finished = true; // ② 发出停止信号
  }

  @Override
  public void run() {
    while (!finished) { // ③ 检测条件变量
      // do dirty work   // ④业务代码
    }
  }
}

class Proxy {
  boolean started = false;
  // 采集线程
  Thread rptThread;
  // 启动采集功能
  synchronized void start() {
    // 不允许同时启动多个采集线程
    if (started) {
      return;
    }
    started = true;
    rptThread =
        new Thread(
            () -> {
              while (!Thread.currentThread().isInterrupted()) {
                // 省略采集、回传实现
                // doSth();
                // 每隔两秒钟采集、回传一次数据
                try {
                  Thread.sleep(2000);
                } catch (InterruptedException e) {
                  // 重新设置线程中断状态
                  Thread.currentThread().interrupt();
                }
              }
              // 执行到此处说明线程马上终止
              started = false;
            });
    rptThread.start();
  }
  // 终止采集功能
  synchronized void stop() {
    rptThread.interrupt();
  }
}

class ThreadPoolStopThreadDemo {

  public static void main(String[] args) throws Exception {
    ExecutorService service = Executors.newFixedThreadPool(2);

    for (int i = 0; i < 10000; i++) {
      service.submit(
          new Runnable() {
            @Override
            public void run() {
              try {
                System.out.println(new Random().nextInt(100));
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          });
    }
    service.shutdown();
    // service.shutdownNow();
    service.awaitTermination(1, TimeUnit.DAYS);
  }
}
