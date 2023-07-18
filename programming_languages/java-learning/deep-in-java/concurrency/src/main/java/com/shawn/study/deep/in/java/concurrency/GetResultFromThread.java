package com.shawn.study.deep.in.java.concurrency;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GetResultFromThread {

  public static void main(String[] args) throws Exception {
    getResultByCallableAndFuture();

    getResultByCallableAndFutureTask();

    getResultBySleep();

    getResultByJoin();

    getResultByLoop();

    getResultByNotifyAndWait();

    getResultByLockAndCondition();

    getResultByBlockQueue();

    getResultByCountDownLatch();

    getResultByCycleBarrier();

    getResultBySemaphore();
  }

  private static void getResultByCallableAndFuture() throws Exception {
    ExecutorService executor = Executors.newCachedThreadPool();
    Future<Integer> future = executor.submit(() -> Sum.sum(100));

    System.out.printf("get result by Callable + Future: %d\n", future.get());
    executor.shutdown();
  }

  private static void getResultByCallableAndFutureTask() throws Exception {
    ExecutorService executor = Executors.newCachedThreadPool();
    FutureTask<Integer> futureTask = new FutureTask<>(() -> Sum.sum(100));
    executor.submit(futureTask);
    System.out.printf("get result by Callable + FutureTask: %d\n", futureTask.get());
    executor.shutdown();
  }

  private static int sum_sleep = 0;

  private static void getResultBySleep() throws Exception {
    Thread thread = new Thread(() -> sum_sleep = Sum.sum(100));
    thread.start();
    TimeUnit.SECONDS.sleep(1);
    System.out.printf("get result by thread.sleep: %d\n", sum_sleep);
  }

  private static int sum_join = 0;

  private static void getResultByJoin() throws Exception {

    Thread thread = new Thread(() -> sum_join = Sum.sum(100));
    thread.start();
    thread.join();
    System.out.printf("get result by thread.join: %d\n", sum_join);
  }

  private static int sum_loop = 0;
  private static volatile boolean flag;

  private static void getResultByLoop() throws Exception {
    Thread thread =
        new Thread(
            () -> {
              sum_loop = Sum.sum(100);
              flag = true;
            });
    thread.start();
    int i = 0;
    while (!flag) {
      i++;
    }
    System.out.printf("get result by loopLock: %d\n", sum_loop);
  }

  private static void getResultByNotifyAndWait() throws Exception {
    NotifyAndWaitTest test = new NotifyAndWaitTest();
    new Thread(test::sum_wait_notify).start();
    System.out.printf("get result by NotifyAndWait: %d\n", test.getSum());
  }

  private static class NotifyAndWaitTest {

    private Integer sum = null;

    private synchronized void sum_wait_notify() {
      sum = Sum.sum(100);
      notifyAll();
    }

    private synchronized Integer getSum() {
      while (sum == null) {
        try {
          wait();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return sum;
    }
  }

  private static void getResultByLockAndCondition() throws Exception {
    LockAndConditionTest test = new LockAndConditionTest();
    new Thread(test::sum).start();
    System.out.printf("get result by lock and condition: %d\n", test.getSum());
  }

  private static class LockAndConditionTest {

    private Integer sum = null;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void sum() {
      try {
        lock.lock();
        sum = Sum.sum(100);
        condition.signalAll();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }

    public Integer getSum() {
      try {
        lock.lock();
        while (Objects.isNull(sum)) {
          try {
            condition.await();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
      return sum;
    }
  }

  private static void getResultByBlockQueue() throws Exception {
    BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
    new Thread(() -> queue.offer(Sum.sum(100))).start();
    System.out.printf("get result by blocking queue: %d\n", queue.take());
  }

  private static int sum_countDownLatch = 0;

  private static void getResultByCountDownLatch() {
    CountDownLatch latch = new CountDownLatch(1);

    new Thread(
            () -> {
              sum_countDownLatch = Sum.sum(100);
              latch.countDown();
            })
        .start();
    try {
      latch.await();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.printf("get result by countDownLatch: %d\n", sum_countDownLatch);
  }

  private static int sum_cyclicBarrier = 0;

  private static void getResultByCycleBarrier() {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    new Thread(
            () -> {
              sum_cyclicBarrier = Sum.sum(100);
              try {
                cyclicBarrier.await();
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
    try {
      cyclicBarrier.await();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.printf("get result by cyclicBarrier: %d\n", sum_cyclicBarrier);
  }

  private static int sum_semaphore = 0;

  private static void getResultBySemaphore() {
    Semaphore semaphore = new Semaphore(0);
    new Thread(
            () -> {
              sum_semaphore = Sum.sum(100);
              semaphore.release();
            })
        .start();

    try {
      semaphore.acquire();
      System.out.printf("get result by semaphore: %d\n", sum_semaphore);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

class Sum {
  private Sum() {}

  public static int sum(int n) {
    int sum = 0;
    for (int i = 0; i < n; i++) {
      sum += n;
    }
    return sum;
  }
}
