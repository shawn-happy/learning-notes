package com.shawn.study.deep.in.java.concurrency.waitnotify;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

public class QueueTests {

  @Test
  public void testQueueWithLoop() {
    Queue queue = new BlockingQueueWithLoop();
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 1; i <= 1000; i++) {
      final int finalNum = i;
      executorService.execute(
          () -> {
            try {
              queue.put(finalNum);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }

    for (int i = 1; i <= 1000; i++) {
      final int finalNum = i;
      executorService.execute(
          () -> {
            try {
              System.out.println(queue.take());
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }

    executorService.shutdown();
  }

  @Test
  public void testQueueWithSync() {
    Queue queue = new BlockingQueueWithSync();
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 1; i <= 1000; i++) {
      final int finalNum = i;
      executorService.execute(
          () -> {
            try {
              queue.put(finalNum);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }

    for (int i = 1; i <= 1000; i++) {
      final int finalNum = i;
      executorService.execute(
          () -> {
            try {
              System.out.println(queue.take());
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }

    executorService.shutdown();
  }

  @Test
  public void testQueueWithCondition() {
    Queue queue = new BlockingQueueWithCondition();
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 1; i <= 1000; i++) {
      final int finalNum = i;
      executorService.execute(
          () -> {
            try {
              queue.put(finalNum);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }

    for (int i = 1; i <= 1000; i++) {
      final int finalNum = i;
      executorService.execute(
          () -> {
            try {
              System.out.println(queue.take());
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }

    executorService.shutdown();
  }
}
