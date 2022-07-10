package com.shawn.study.deep.in.java.concurrency.design;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerDemoTwo {

  public static void main(String[] args) {
    BlockingQueue<Integer> sharedQ = new LinkedBlockingQueue<Integer>();

    ProducerThread p = new ProducerThread(sharedQ);
    ConsumerThead c = new ConsumerThead(sharedQ);

    p.start();
    c.start();
  }
}

class ProducerThread extends Thread {
  private BlockingQueue<Integer> sharedQueue;

  public ProducerThread(BlockingQueue<Integer> aQueue) {
    super("PRODUCER");
    this.sharedQueue = aQueue;
  }

  @Override
  public void run() {
    // no synchronization needed
    for (int i = 0; i < 10; i++) {
      try {
        System.out.println(getName() + " produced " + i);
        sharedQueue.put(i);
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class ConsumerThead extends Thread {
  private BlockingQueue<Integer> sharedQueue;

  public ConsumerThead(BlockingQueue<Integer> aQueue) {
    super("CONSUMER");
    this.sharedQueue = aQueue;
  }

  @Override
  public void run() {
    try {
      while (true) {
        Integer item = sharedQueue.take();
        System.out.println(getName() + " consumed " + item);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
