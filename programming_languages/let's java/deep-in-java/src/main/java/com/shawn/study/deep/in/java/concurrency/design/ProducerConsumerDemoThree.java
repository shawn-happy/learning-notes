package com.shawn.study.deep.in.java.concurrency.design;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class ProducerConsumerDemoThree {

  public static void main(String[] args) {
    Semaphore notFull = new Semaphore(10);
    Semaphore notEmpty = new Semaphore(0);
    Vector queue = new Vector();
    Producer1 producer = new Producer1("生产者线程", notFull, notEmpty, queue);
    Consumer1 consumer = new Consumer1("消费者线程", notFull, notEmpty, queue);

    producer.start();
    consumer.start();
  }
}

class Producer1 extends Thread {

  private Semaphore notFull;
  private Semaphore notEmpty;
  //    private Semaphore mutex;
  private Vector queue;
  private final int SIZE = 4;

  // ,Semaphore mutex
  public Producer1(String name, Semaphore notFull, Semaphore notEmpty, Vector queue) {
    super(name);
    this.notFull = notFull;
    this.notEmpty = notEmpty;
    //        this.mutex = mutex;
    this.queue = queue;
  }

  @Override
  public void run() {
    for (int i = 0; i < 7; i++) {
      try {
        // 非满阻塞
        log(" not full is waiting for permit");
        notFull.acquire();
        log(" acquired a permit");
        log(" add value! ");
        //                    mutex.acquire();
        queue.add(i);
        notEmpty.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void log(String msg) {
    System.out.println(Thread.currentThread().getName() + "  " + msg);
  }
}

class Consumer1 extends Thread {

  private Semaphore notFull;
  private Semaphore notEmpty;
  //    private Semaphore mutex;
  private Vector queue;
  private final int SIZE = 4;

  // ,Semaphore mutex
  public Consumer1(String name, Semaphore notFull, Semaphore notEmpty, Vector queue) {
    super(name);
    this.notFull = notFull;
    this.notEmpty = notEmpty;
    //        this.mutex = mutex;
    this.queue = queue;
  }

  @Override
  public void run() {
    for (int i = 0; i < 7; i++) {
      try {
        // 非满阻塞
        log(" not empty is waiting for permit");
        notEmpty.acquire();
        log(" acquired a permit");
        log(" getValue! ");
        //                    mutex.acquire();
        log(queue.get(i) + "");
        notFull.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void log(String msg) {
    System.out.println(Thread.currentThread().getName() + " 消费 " + msg);
  }
}
