package com.shawn.study.deep.in.java.concurrency.design;

import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerConsumerDemoOne {}

class Producer implements Runnable {

  private final Vector queue;
  private final int SIZE;

  public Producer(Vector queue, int size) {
    this.queue = queue;
    this.SIZE = size;
  }

  @Override
  public void run() {
    for (int i = 0; i < 7; i++) {
      System.out.println(" Produced: " + i);
      try {
        produce(i);
      } catch (InterruptedException e) {
        Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, e);
      }
    }
  }

  public void produce(int i) throws InterruptedException {
    // wait if queue is full
    while (queue.size() == SIZE) {
      synchronized (queue) {
        System.out.println(
            "queue is full "
                + Thread.currentThread().getName()
                + " is waiting,size: "
                + queue.size());
        queue.wait();
      }
    }
    // notify consumer take element
    synchronized (queue) {
      queue.add(i);
      queue.notifyAll();
    }
  }
}

class Consumer implements Runnable {

  private final Vector queue;
  private final int SIZE;

  public Consumer(Vector queue, int size) {
    this.queue = queue;
    this.SIZE = size;
  }

  @Override
  public void run() {
    while (true) {
      try {
        System.out.println("Consumed: " + consume());
        TimeUnit.MILLISECONDS.sleep(50);
      } catch (InterruptedException e) {
        Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
      }
    }
  }

  public int consume() throws InterruptedException {
    // wait if queue isEmpty
    while (queue.isEmpty()) {
      synchronized (queue) {
        System.out.println(
            "Queue is empty "
                + Thread.currentThread().getName()
                + " is waiting , size: "
                + queue.size());

        queue.wait();
      }
    }
    synchronized (queue) {
      queue.notifyAll();
      return (Integer) queue.remove(0);
    }
  }
}
