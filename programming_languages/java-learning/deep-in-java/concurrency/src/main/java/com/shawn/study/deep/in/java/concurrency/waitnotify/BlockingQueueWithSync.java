package com.shawn.study.deep.in.java.concurrency.waitnotify;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueueWithSync implements Queue {

  private int[] elements;
  private int head;
  private int tail;
  private volatile int size; // 队列元素个数

  public BlockingQueueWithSync() {
    this(10);
  }

  public BlockingQueueWithSync(int capacity) {
    this.elements = new int[capacity];
    this.head = 0;
    this.tail = 0;
    this.size = 0;
  }

  @Override
  public synchronized void put(int e) throws InterruptedException {
    while (size == elements.length) {
      this.wait();
    }
    elements[tail] = e;
    tail++;
    if (tail == elements.length) {
      tail = 0;
    }
    size++;
    this.notifyAll();
  }

  @Override
  public synchronized int take() throws InterruptedException {
    while (isEmpty()) {
      this.wait();
    }
    int e = elements[head];
    if (++head == elements.length) {
      head = 0;
    }
    --size;
    this.notifyAll();
    return e;
  }

  @Override
  public synchronized int size() {
    return size;
  }

  @Override
  public synchronized boolean isEmpty() {
    return size == 0;
  }
}
