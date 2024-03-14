package io.github.shawn.datastructure.stack;

import java.util.EmptyStackException;
import java.util.Iterator;

/**
 * @author shawn
 * @since 2020/10/11
 */
public class ArrayStack<T> implements Stack<T>{

  private int size;
  private int capacity;
  private Object[] data;
  private static final int DEFAULT_CAPACITY = 2 << 3;

  public ArrayStack() {
    this(DEFAULT_CAPACITY);
  }

  public ArrayStack(int capacity) {
    this.size = 0;
    this.capacity = capacity;
    this.data = new Object[capacity];
  }

  // 返回栈中的元素个数
  @Override
  public int size() {
    return size;
  }

  // 从栈中弹出一个元素
  // 如果栈空就抛出一个异常
  @Override
  public T pop() {
    if (isEmpty()) {
      throw new EmptyStackException();
    }
    T element = element(--size);
    data[size] = null;
    return element;
  }

  // 查看栈顶元素(并不移除)
  // 如果栈空就抛出一个异常
  @Override
  public T peek() {
    if (isEmpty()) {
      throw new EmptyStackException();
    }
    return element(size - 1);
  }

  // 将一个元素入栈
  @Override
  public void push(T elem) {
    // 扩容
    if (size == capacity) {
      doubleCapacity();
    }
    data[size++] = elem;
  }

  private void doubleCapacity() {
    int newCapacity = capacity << 1;
    Object[] newData = new Object[newCapacity];
    System.arraycopy(data, 0, newData, 0, size);
    data = newData;
  }

  @SuppressWarnings("unchecked")
  private T element(int index) {
    return (T) data[index];
  }

  // 支持以迭代器方式对栈进行遍历
  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private int index = size - 1;

      public boolean hasNext() {
        return index >= 0;
      }

      public T next() {
        return element(index--);
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
