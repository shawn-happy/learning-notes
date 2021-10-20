package com.shawn.study.datastructure.linkedlist;

import java.util.Iterator;

/**
 * 单向链表
 *
 * @author shawn
 * @since 2020/10/1
 */
public class SingleLinkedList<T> implements Iterable<T> {

  /** 头结点 */
  private Node<T> head;
  /** 单向链表实际大小 */
  private int size;

  public void add(T t) {
    Node<T> newNode = new Node<>(t);
    if (head == null) {
      head = newNode;
      size++;
      return;
    }
    Node<T> p = head;
    while (p.next != null) {
      p = p.next;
    }
    p.next = newNode;
    size++;
  }

  public void add(int index, T t) {
    Node<T> node = findByIndex(index);
    addBefore(node, t);
  }

  public void addFirst(T t) {
    Node<T> newNode = new Node<>(t);
    if (head == null) {
      head = newNode;
      size++;
      return;
    }
    newNode.next = head;
    head = newNode;
    size++;
  }

  public void addLast(T t) {
    add(t);
  }

  public void addBefore(T data, T value) {
    addBefore(findByItem(data), value);
  }

  public void addAfter(T data, T value) {
    addAfter(findByItem(data), value);
  }

  public void set(int index, T t) {
    Node<T> node = findByIndex(index);
    node.data = t;
  }

  public T remove(int index) {
    Node<T> node = findByIndex(index);
    boolean b = deleteByNode(node);
    if (b) {
      size--;
      return node.data;
    }
    return null;
  }

  public boolean remove(Object o) {
    Node<T> node = findByItem(o);
    boolean b = deleteByNode(node);
    if (b) {
      size--;
    }
    return b;
  }

  public T get(int index) {
    checkIndex(index);
    Node<T> p = head;
    int pos = 0;
    while (p != null && index != pos) {
      p = p.next;
      pos++;
    }
    return p.data;
  }

  public void clear() {
    Node<T> p = head;
    while (p != null) {
      Node<T> q = p.next;
      p.data = null;
      p.next = null;
      p = q;
    }
    size = 0;
  }

  public int indexOf(Object o) {
    Node<T> p = head;
    int index = 0;
    if (o == null) {
      while (p != null && p.data != null) {
        p = p.next;
        index++;
      }
    } else {
      while (p != null && !p.data.equals(o)) {
        p = p.next;
        index++;
      }
    }
    if (p == null) {
      return -1;
    }
    return index;
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
        return get(index++);
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public String toString() {
    if (size == 0 || head == null) {
      return "[]";
    }
    StringBuilder builder = new StringBuilder();
    Node<T> p = head;
    while (p.next != null) {
      builder.append(p.data).append(", ");
      p = p.next;
    }
    builder.append(p.data);
    return String.format("Size: %d, Data: [%s]", size, builder.toString());
  }

  private void addBefore(Node<T> node, T t) {
    if (node == null) {
      return;
    }
    if (node.equals(head)) {
      addFirst(t);
      return;
    }
    Node<T> p = head;
    while (p != null && !p.next.equals(node)) {
      p = p.next;
    }
    if (p == null) {
      return;
    }
    Node<T> newNode = new Node<>(t);
    newNode.next = node;
    p.next = newNode;
    size++;
  }

  private void addAfter(Node<T> node, T t) {
    if (node == null) {
      return;
    }
    Node<T> newNode = new Node<>(t);
    newNode.next = node.next;
    node.next = newNode;
    size++;
  }

  private Node<T> findByItem(Object item) {
    Node<T> p = head;
    if (item == null) {
      while (p != null && p.data != null) {
        p = p.next;
      }
    } else {
      while (p != null && !p.data.equals(item)) {
        p = p.next;
      }
    }

    return p;
  }

  private Node<T> findByIndex(int index) {
    checkIndex(index);
    Node<T> p = head;
    int pos = 0;
    while (p != null && index != pos) {
      p = p.next;
      pos++;
    }
    return p;
  }

  private boolean deleteByNode(Node node) {
    if (node == null || head == null) {
      return false;
    }
    if (node == head) {
      head = head.next;
      return true;
    }
    Node p = head;
    while (p != null && p.next != node) {
      p = p.next;
    }
    if (p == null) {
      return false;
    }
    p.next = p.next.next;
    return true;
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
    }
  }

  private static class Node<T> {
    private T data;
    private Node<T> next;

    public Node(T data) {
      this(data, null);
    }

    public Node(T data, Node<T> next) {
      this.data = data;
      this.next = next;
    }

    @Override
    public String toString() {
      if (data == null) {
        return "";
      }
      return data.toString();
    }
  }
}
