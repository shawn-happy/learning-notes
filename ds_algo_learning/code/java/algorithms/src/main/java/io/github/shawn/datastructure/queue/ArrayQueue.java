package io.github.shawn.datastructure.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 基于数组实现的顺序队列（涉及到数据迁移）
 *
 * @author shawn
 * @since 2020/10/14
 */
public class ArrayQueue<T> implements Queue<T> {

  private Object[] data;
  private int head;
  private int tail;
  private int size;
  private int capacity;
  private static final int DEFAULT_CAPACITY = 10;

  public ArrayQueue() {
    this(DEFAULT_CAPACITY);
  }

  public ArrayQueue(int capacity) {
    this.data = new Object[capacity];
    this.head = 0;
    this.tail = 0;
    this.capacity = capacity;
  }

  @Override
  public void offer(T elem) {
    enqueue(elem, true);
  }

  public void enqueue(T elem, boolean moved) {
    if (elem == null) {
      throw new NullPointerException();
    }
    if (isFull()) {
      doubleCapacity();
    }
    if (head != 0 && tail == capacity && moved) {
      System.arraycopy(data, head, data, 0, tail - head);
      for (int i = 0; i < head; i++) {
        data[data.length - i - 1] = null;
      }
      tail -= head;
      head = 0;
    }
    data[tail++] = elem;
    size++;
  }

  private void doubleCapacity() {
    int newCapacity = capacity << 1;
    if (newCapacity < 0) {
      throw new IllegalStateException("illegal Capacity: " + newCapacity);
    }
    Object[] newData = new Object[newCapacity];
    System.arraycopy(data, head, newData, 0, tail - head);
    data = newData;
  }

  @Override
  public T poll() {
    if (isEmpty()) {
      throw new NoSuchElementException("queue is empty!");
    }
    T t = element(head);
    data[head] = null;
    head++;
    size--;
    return t;
  }

  @Override
  public T peek() {
    if (isEmpty()) {
      throw new NoSuchElementException("queue is empty!");
    }
    T t = element(head);
    return t;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return head == tail;
  }

  private boolean isFull() {
    if (head == 0 && tail == capacity) {
      return true;
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  private T element(int index) {
    return (T) data[index];
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private int index = head;

      @Override
      public boolean hasNext() {
        return index < tail;
      }

      @Override
      public T next() {
        return element(index++);
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
