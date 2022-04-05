package com.shawn.study.deep.in.java.concurrency.produceandconsume;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProduceAndConsumeDemo {

  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    Container container = new Container();
    executorService.execute(container::produce);
    executorService.execute(container::consume);
    executorService.shutdown();
  }

  private static class Container {
    private List<Integer> data = new LinkedList<>();
    private static final int MAX_SIZE = 5;
    private static final Random random = new Random();

    public void produce() {
      while (true) {
        synchronized (this) {
          try {
            while (data.size() >= MAX_SIZE) {
              wait();
            }
            int value = random.nextInt(100);
            System.out.printf("线程[%s] 正在生产数据 : %d\n", Thread.currentThread().getName(), value);
            data.add(value);

            // 唤起消费线程
            notifyAll();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }

    public void consume() {
      while (true) {
        synchronized (this) {
          try {
            while (data.isEmpty()) {
              wait();
            }
            int value = data.remove(0);
            System.out.printf("线程[%s] 正在消费数据 : %d\n", Thread.currentThread().getName(), value);

            // 唤起消费线程
            notifyAll();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
