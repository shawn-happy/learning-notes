package io.github.shawn.datastructure.queue;

import java.util.Iterator;

/**
 * 双端循环队列
 *
 * @author shawn
 * @since 2020/10/17
 */
public class ArrayCircularDeque<T> implements Deque<T> {

  private int head;
  private int tail;
  private Object[] data;
  private int n;

  private static final int DEFAULT_CAPACITY = 10;

  public ArrayCircularDeque() {
    this(DEFAULT_CAPACITY);
  }

  public ArrayCircularDeque(int capacity) {
    data = new Object[capacity + 1];
    head = 0;
    tail = 0;
    n = capacity + 1;
  }

  @Override
  public void offerFirst(T t) {
    if (isFull()) {
      throw new RuntimeException("deque is full");
    }
    head = (head - 1 + n) % n;
    data[head] = t;
  }

  @Override
  public void offerLast(T t) {
    if (isFull()) {
      throw new RuntimeException("deque is full");
    }
    data[tail] = t;
    tail = (tail + 1) % n;
  }

  @Override
  public T pollFirst() {
    if (isEmpty()) {
      throw new RuntimeException("deque is empty");
    }
    T elements = elements(head);
    data[head] = null;
    head = (head + 1) % n;
    return elements;
  }

  @Override
  public T pollLast() {
    if (isEmpty()) {
      throw new RuntimeException("deque is empty");
    }
    T elements = elements(tail);
    data[tail] = null;
    tail = (tail - 1 + n) % n;
    return elements;
  }

  @Override
  public T peekFirst() {
    if (isEmpty()) {
      throw new RuntimeException("deque is empty");
    }
    return elements(head);
  }

  @Override
  public T peekLast() {
    if (isEmpty()) {
      throw new RuntimeException("deque is empty");
    }
    return elements((tail - 1 + n) % n);
  }

  @Override
  public void offer(T elem) {
    offerLast(elem);
  }

  @Override
  public T poll() {
    return pollFirst();
  }

  @Override
  public T peek() {
    return peekFirst();
  }

  @Override
  public int size() {
    return (tail + n - head) >= n ? tail - head : tail + n - head;
  }

  @Override
  public boolean isEmpty() {
    return head == tail;
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
        return elements(p++);
      }
    };
  }

  @SuppressWarnings("unchecked")
  private T elements(int index) {
    return (T) data[index];
  }
}
