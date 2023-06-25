package com.shawn.study.deep.in.java.concurrency.waitnotify;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueWithCondition implements Queue {

  private int[] elements;
  private int head;
  private int tail;
  private volatile int size; // 队列元素个数
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition notEmpty = lock.newCondition();
  private final Condition notFull = lock.newCondition();

  public BlockingQueueWithCondition() {
    this(10);
  }

  public BlockingQueueWithCondition(int capacity) {
    this.elements = new int[capacity];
    this.head = 0;
    this.tail = 0;
    this.size = 0;
  }

  @Override
  public void put(int e) throws InterruptedException {
    lock.lockInterruptibly();
    try {
      while (size == elements.length) {
        notFull.await();
      }
      elements[tail] = e;
      tail++;
      if (tail == elements.length) {
        tail = 0;
      }
      size++;
      notEmpty.signalAll();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int take() throws InterruptedException {
    lock.lockInterruptibly();
    try {
      while (isEmpty()) {
        notEmpty.await();
      }
      int e = elements[head];
      if (++head == elements.length) {
        head = 0;
      }
      --size;
      notFull.signalAll();
      return e;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int size() {
    try {
      lock.lock();
      return size;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean isEmpty() {
    try {
      lock.lock();
      return size == 0;
    } finally {
      lock.unlock();
    }
  }
}
