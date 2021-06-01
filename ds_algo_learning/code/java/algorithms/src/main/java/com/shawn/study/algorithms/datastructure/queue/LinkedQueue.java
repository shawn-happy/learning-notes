package com.shawn.study.algorithms.datastructure.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 基于链表实现的队列（无界，不需要判断队满）
 *
 * @author shawn
 * @since 2020/10/14
 */
public class LinkedQueue<T> implements Queue<T> {

  private Node<T> head;
  private Node<T> tail;

  private int size;

  @Override
  public void offer(T elem) {
    if (tail == null) {
      Node<T> newNode = new Node<>(elem, null);
      head = newNode;
      tail = newNode;
    } else {
      tail.next = new Node<>(elem, null);
      tail = tail.next;
    }
    size++;
  }

  @Override
  public T poll() {
    if (isEmpty()) {
      throw new NoSuchElementException("queue is empty!");
    }
    T value = head.data;
    head = head.next;
    if (head == null) {
      tail = null;
    }
    size--;
    return value;
  }

  @Override
  public T peek() {
    if (isEmpty()) {
      throw new NoSuchElementException("queue is empty!");
    }
    T value = head.data;
    return value;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return head == null;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private Node<T> p = head;

      @Override
      public boolean hasNext() {
        return p != null;
      }

      @Override
      public T next() {
        T data = p.data;
        p = p.next;
        return data;
      }
    };
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
