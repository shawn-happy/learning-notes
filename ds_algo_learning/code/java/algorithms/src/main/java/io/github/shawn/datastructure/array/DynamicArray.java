package io.github.shawn.datastructure.array;

import java.util.Iterator;

/**
 * 动态数组 {@link java.util.ArrayList}
 *
 * @author shawn
 * @since 2020/9/30
 */
public class DynamicArray<T> implements Iterable<T> {

  /** 数组的实际大小 */
  private int size;
  /** 数组的容量 */
  private int capacity;

  /** 内部维护的数组 */
  private Object[] data;

  /** 默认的数组容量 */
  private static final int DEFAULT_CAPACITY = 2 << 3;

  public DynamicArray() {
    this(DEFAULT_CAPACITY);
  }

  public DynamicArray(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException("Illegal Capacity: " + capacity);
    }
    this.capacity = capacity;
    this.size = 0;
    this.data = new Object[capacity];
  }

  public void add(T t) {
    resize(size + 1);
    data[size] = t;
    size++;
  }

  public void add(int index, T t) {
    checkIndexForAdd(index);
    resize(size + 1);
    System.arraycopy(data, index, data, index + 1, size - index);
    data[index] = t;
    size++;
  }

  public void set(int index, T t) {
    checkIndex(index);
    data[index] = t;
  }

  public T remove(int index) {
    checkIndex(index);
    T t = data(index);
    System.arraycopy(data, index + 1, data, index, size - index - 1);
    data[--size] = null;
    return t;
  }

  public boolean remove(Object o) {
    int i = indexOf(o);
    if (i == -1) {
      return false;
    }
    remove(i);
    return true;
  }

  public T get(int index) {
    checkIndex(index);
    return data(index);
  }

  public void clear() {
    for (int i = 0; i < size; i++) {
      data[i] = null;
    }
    size = 0;
  }

  public int indexOf(Object o) {
    if (o == null) {
      for (int i = 0; i < size; i++) {
        if (data[i] == null) {
          return i;
        }
      }
    } else {
      for (int i = 0; i < size; i++) {
        if (o.equals(data[i])) {
          return i;
        }
      }
    }
    return -1;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public boolean contains(T t) {
    return indexOf(t) != -1;
  }

  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private int index = 0;

      public boolean hasNext() {
        return index < size;
      }

      public T next() {
        return data(index++);
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public String toString() {
    if (size == 0) {
      return "[]";
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < size - 1; i++) {
      builder.append(data[i] + ", ");
    }
    builder.append(data[size - 1]);
    return String.format("Size: %d, Data: [%s]", size, builder.toString());
  }

  @SuppressWarnings("unchecked")
  private T data(int index) {
    return (T) data[index];
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new ArrayIndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
    }
  }

  private void checkIndexForAdd(int index) {
    if (index < 0 || index > size) {
      throw new ArrayIndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
    }
  }

  private void resize(int length) {
    if (length >= capacity) {
      if (capacity == 0) {
        capacity = 1;
      } else {
        capacity = capacity << 1;
      }
      Object[] newData = new Object[capacity];
      System.arraycopy(data, 0, newData, 0, size);
      data = newData;
    }
  }
}
