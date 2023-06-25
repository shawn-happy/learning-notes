package com.shawn.study.deep.in.java.concurrency.waitnotify;

public class BlockingQueueWithLoop implements Queue {

  private int[] elements;
  private int head;
  private int tail;
  private volatile int size; // 队列元素个数

  public BlockingQueueWithLoop() {
    this(10);
  }

  public BlockingQueueWithLoop(int capacity) {
    this.elements = new int[capacity];
    this.head = 0;
    this.tail = 0;
    this.size = 0;
  }

  @Override
  public void put(int e) throws InterruptedException {
    while (size == elements.length) {}
    synchronized (this) {
      if (size == elements.length) {
        return;
      }
      elements[tail] = e;
      tail++;
      if (tail == elements.length) {
        tail = 0;
      }
      size++;
    }
  }

  @Override
  public int take() throws InterruptedException {
    while (true) {
      while (size <= 0) {}

      synchronized (this) {
        if (size > 0) {
          int e = elements[head];
          head++;
          if (head == elements.length) {
            head = 0;
          }
          size--;
          return e;
        }
      }
    }
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
