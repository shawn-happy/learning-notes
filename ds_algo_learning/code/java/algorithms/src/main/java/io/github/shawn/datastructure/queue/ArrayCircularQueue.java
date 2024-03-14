package io.github.shawn.datastructure.queue;

import java.util.Iterator;

/**
 * 循环队列
 *
 * @author shawn
 * @since 2020/10/17
 */
public class ArrayCircularQueue<T> implements Queue<T> {

  private Object[] data;
  private int head;
  private int tail;
  private int n;
  private static final int DEFAULT_CAPACITY = 10;

  public ArrayCircularQueue() {
    this(DEFAULT_CAPACITY);
  }

  public ArrayCircularQueue(int capacity) {
    data = new Object[capacity + 1];
    head = 0;
    tail = 0;
    n = capacity + 1;
  }

  @Override
  public void offer(T elem) {
    if (isFull()) {
      throw new RuntimeException("Queue is full");
    }
    data[tail] = elem;
    tail = adjustIndex(tail);
  }

  @Override
  public T poll() {
    if (isEmpty()) {
      throw new RuntimeException("Queue is empty");
    }
    T element = element(head);
    head = adjustIndex(head);
    return element;
  }

  @Override
  public T peek() {
    if (isEmpty()) {
      throw new RuntimeException("Queue is empty");
    }
    return element(head);
  }

  @Override
  public int size() {
    return (tail + n - head) >= n ? tail - head : tail + n - head;
  }

  @Override
  public boolean isEmpty() {
    return tail == head;
  }

  public boolean isFull() {
    return (tail + 1) % n == head;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private int p = head;

      @Override
      public boolean hasNext() {
        return p != tail;
      }

      @Override
      public T next() {
        return element(p++);
      }
    };
  }

  private int adjustIndex(int index) {
    return (index + 1) % n;
  }

  @SuppressWarnings("unchecked")
  private T element(int index) {
    return (T) data[index];
  }
}
