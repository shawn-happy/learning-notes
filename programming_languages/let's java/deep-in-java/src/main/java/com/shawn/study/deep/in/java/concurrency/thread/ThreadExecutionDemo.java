package com.shawn.study.deep.in.java.concurrency.thread;

import java.lang.Thread.State;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 线程执行demo
 *
 * @author shawn
 */
public class ThreadExecutionDemo {

  public static void main(String[] args) throws Exception {
    execByCyclicBarrier();
    TimeUnit.SECONDS.sleep(4);
    execByCountDownLatch();
    TimeUnit.SECONDS.sleep(4);
    execByWait();
    execBySleep();
    execByLoop();
    execByJoin();
  }

  private static void execByCyclicBarrier() {
    System.out.println("Thread execute by CyclicBarrier");
    CyclicBarrier latch = new CyclicBarrier(3);
    Thread t1 = new Thread(new ExecByCyclicBarrier(latch, 1), "t1");
    Thread t2 = new Thread(new ExecByCyclicBarrier(latch, 2), "t2");
    Thread t3 = new Thread(new ExecByCyclicBarrier(latch, 3), "t3");
    t1.start();
    t2.start();
    t3.start();
  }

  private static void execByCountDownLatch() {
    System.out.println("Thread execute by CountDownLatch");
    CountDownLatch latch = new CountDownLatch(3);
    Thread t1 = new Thread(new ExecByCountDownLatch(latch, 1), "t1");
    Thread t2 = new Thread(new ExecByCountDownLatch(latch, 2), "t2");
    Thread t3 = new Thread(new ExecByCountDownLatch(latch, 3), "t3");
    t1.start();
    t2.start();
    t3.start();
  }

  private static void execByWait() {
    System.out.println("Thread execute by wait");
    Thread t1 = new Thread(ThreadExecutionDemo::action, "t1");
    Thread t2 = new Thread(ThreadExecutionDemo::action, "t2");
    Thread t3 = new Thread(ThreadExecutionDemo::action, "t3");
    execStartAndWait(t1);
    execStartAndWait(t2);
    execStartAndWait(t3);
  }

  private static void execStartAndWait(Thread thread) {
    if (State.NEW.equals(thread.getState())) {
      thread.start();
    }

    while (thread.isAlive()) {
      synchronized (thread) {
        try {
          thread.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static void execBySleep() throws InterruptedException {
    System.out.println("Thread execute by Sleep");
    Thread t1 = new Thread(ThreadExecutionDemo::action, "t1");
    Thread t2 = new Thread(ThreadExecutionDemo::action, "t2");
    Thread t3 = new Thread(ThreadExecutionDemo::action, "t3");
    t1.start();
    TimeUnit.SECONDS.sleep(1);
    t2.start();
    TimeUnit.SECONDS.sleep(1);
    t3.start();
    TimeUnit.SECONDS.sleep(1);
  }

  private static void execByLoop() {
    System.out.println("Thread execute by loop lock");
    Thread t1 = new Thread(ThreadExecutionDemo::action, "t1");
    Thread t2 = new Thread(ThreadExecutionDemo::action, "t2");
    Thread t3 = new Thread(ThreadExecutionDemo::action, "t3");
    t1.start();
    // 自旋
    while (t1.isAlive()) {}
    t2.start();
    while (t2.isAlive()) {}
    t3.start();
    while (t3.isAlive()) {}
  }

  private static void execByJoin() throws InterruptedException {
    System.out.println("Thread execute by join method");
    Thread t1 = new Thread(ThreadExecutionDemo::action, "t1");
    Thread t2 = new Thread(ThreadExecutionDemo::action, "t2");
    Thread t3 = new Thread(ThreadExecutionDemo::action, "t3");
    t1.start();
    t1.join();
    t2.start();
    t2.join();
    t3.start();
    t3.join();
  }

  private static void action() {
    System.out.printf("当前线程[%s], 正在执行。。。\n", Thread.currentThread().getName());
  }

  private static class ExecByCountDownLatch implements Runnable {

    private CountDownLatch latch;

    private int delay;

    public ExecByCountDownLatch(CountDownLatch latch, int delay) {
      this.latch = latch;
      this.delay = delay;
    }

    @Override
    public void run() {
      try {
        TimeUnit.SECONDS.sleep(delay);
        action();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        latch.countDown();
      }
    }
  }

  private static class ExecByCyclicBarrier implements Runnable {

    private CyclicBarrier barrier;

    private int delay;

    public ExecByCyclicBarrier(CyclicBarrier barrier, int delay) {
      this.barrier = barrier;
      this.delay = delay;
    }

    @Override
    public void run() {
      try {
        TimeUnit.SECONDS.sleep(delay);
        action();
        barrier.await();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
