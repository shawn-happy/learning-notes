package com.shawn.study.datastructure.stack;

import java.util.EmptyStackException;
import java.util.Iterator;

/**
 * @author shawn
 * @since 2020/10/11
 */
public class ListStack<T> implements Stack<T> {

  private Node<T> top;

  private int size;

  // 返回栈中的元素个数
  @Override
  public int size() {
    return size;
  }

  // 从栈中弹出一个元素
  // 如果栈空就抛出一个异常
  @Override
  public T pop() {
    if (top == null) {
      throw new EmptyStackException();
    }
    T value = top.data;
    top = top.next;
    size--;
    return value;
  }

  // 查看栈顶元素(并不移除)
  // 如果栈空就抛出一个异常
  @Override
  public T peek() {
    if (top == null) {
      throw new EmptyStackException();
    }
    return top.data;
  }

  // 将一个元素入栈
  @Override
  public void push(T elem) {
    Node<T> newNode = new Node<T>(elem, null);
    if (top != null) {
      newNode.next = top;
    }
    top = newNode;
    size++;
  }

  // 支持以迭代器方式对栈进行遍历
  @Override
  public Iterator<T> iterator() {
    // YOUR CODE HERE
    return new Iterator<T>() {
      private int index = 0;

      @Override
      public boolean hasNext() {
        return index < size;
      }

      @Override
      public T next() {
        return get(index++);
      }
    };
  }

  public T get(int index) {
    checkIndex(index);
    Node<T> p = top;
    int pos = 0;
    while (p != null && index != pos) {
      p = p.next;
      pos++;
    }
    return p.data;
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
    }
  }

  private static class Node<T> {

    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
      this.data = data;
      this.next = next;
    }
  }
}
