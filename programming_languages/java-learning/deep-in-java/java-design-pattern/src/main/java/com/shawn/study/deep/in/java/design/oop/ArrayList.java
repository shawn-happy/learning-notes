package com.shawn.study.deep.in.java.design.oop;

/**
 * @author shawn
 * @param <E>
 */
public class ArrayList<E> extends AbstractList<E> {

  /** 内置数组 */
  private E[] datas;

  /** 容量 */
  private int capacity;

  /** 数组实际大小 */
  protected int size;

  /** 默认数组大小 */
  private int DEFAUL_CAPACITY = 10;

  public ArrayList() {
    datas = (E[]) new Object[DEFAUL_CAPACITY];
    this.capacity = DEFAUL_CAPACITY;
  }

  @Override
  protected E[] getDatas() {
    return datas;
  }

  public ArrayList(int capacity) {
    datas = (E[]) new Object[capacity];
    this.capacity = capacity;
  }

  @Override
  public void add(int index, E o) {
    checkIndexForAdd(index);
    // 判断容量是否已满
    if (datas.length == size) {
      // 扩容
      ensureCapacity();
    }
    // 数据搬移
    System.arraycopy(datas, index, datas, index + 1, size - index);
    datas[index] = o;
    size++;
  }

  @Override
  public void ensureCapacity() {
    int size = size();
    int oldCapacity = capacity();
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity > Integer.MAX_VALUE - 8) {
      newCapacity = Integer.MAX_VALUE - 8;
    }
    Object[] newData = new Object[newCapacity];
    System.arraycopy(datas, 0, newData, 0, size);
    datas = (E[]) newData;
  }

  @Override
  public E get(int index) {
    return datas[index];
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public int capacity() {
    return capacity;
  }
}
