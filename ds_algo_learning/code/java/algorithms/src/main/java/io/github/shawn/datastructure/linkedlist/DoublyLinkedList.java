package io.github.shawn.datastructure.linkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 双向链表 参考{@link java.util.LinkedList}
 *
 * @author shawn
 * @since 2020/10/10
 */
public class DoublyLinkedList<T> implements Iterable<T> {

  private Node<T> head;

  private Node<T> tail;

  private int size;

  // 将链表清空，O(n)
  public void clear() {
    Node<T> p = head;
    while (p != null) {
      Node<T> temp = p.next;
      p.next = null;
      p.prev = null;
      p.data = null;
      p = temp;
    }
    head = tail = null;
    size = 0;
  }

  // 返回链表的长度
  public int size() {
    return size;
  }

  // 判断链表是否为空
  public boolean isEmpty() {
    return size() == 0;
  }

  // 向链表的尾部添加一个元素, O(1)
  public void add(T t) {
    addLast(t);
  }

  // 向链表的尾部添加一个元素, O(1)
  public void addLast(T t) {
    if (isEmpty()) {
      head = tail = new Node<>(t, null, null);
      size++;
      return;
    }
    tail.next = new Node<>(t, tail, null);
    tail = tail.next;
    size++;
  }

  // 在链表的头部添加一个元素，O(1)
  public void addFirst(T t) {
    if (isEmpty()) {
      head = tail = new Node<>(t, null, null);
      size++;
      return;
    }
    head.prev = new Node<>(t, null, head);
    head = head.prev;
    size++;
  }

  // 向指定的索引位置添加一个元素
  public void addAt(int index, T data) throws Exception {
    checkIndexForAdd(index);
    if (index == 0) {
      addFirst(data);
      return;
    }
    if (index == size) {
      addLast(data);
      return;
    }
    Node<T> p = head;
    int pos = 0;
    while (p != null && index != pos) {
      p = p.next;
      pos++;
    }
    Node<T> node = new Node<>(data, p, p.next);
    p.next = node;
    p.next.prev = node;
    size++;
  }

  public void addBefore(T item, T value) {
    addBefore(findByItem(item), value);
  }

  public void addAfter(T item, T value) {
    addAfter(findByItem(item), value);
  }

  // 获取第一个节点的值，如果存在的话, O(1)
  public T peekFirst() {
    return isEmpty() ? null : head.data;
  }

  // 获取最后一个节点的值，如果存在的话，O(1)
  public T peekLast() {
    return isEmpty() ? null : tail.data;
  }

  // 移除链表中的头部节点，并返回它的值，O(1)
  public T removeFirst() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    T data = head.data;
    head = head.next;
    --size;
    if (isEmpty()) {
      tail = null;
    } else {
      head.prev = null;
    }
    return data;
  }

  // 移除链表中的最后一个节点，并返回它的值，O(1)
  public T removeLast() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    T data = tail.data;
    tail = tail.prev;
    --size;
    if (isEmpty()) {
      head = null;
    } else {
      tail.next = null;
    }
    return data;
  }

  // 移除链表中的一个指定的节点，O(1)
  // 内部使用
  private T remove(Node<T> node) {
    if (node == null) {
      return null;
    }
    T data = node.data;
    if (node.prev == null) {
      return removeFirst();
    }
    if (node.next == null) {
      return removeLast();
    }
    node.next.prev = node.prev;
    node.prev.next = node.next;
    size--;
    node.data = null;
    node = node.prev = node.next = null;
    return data;
  }

  // 移除指定索引位置的节点，O(n)
  public T removeAt(int index) {
    return remove(findByIndex(index));
  }

  // 在链表中移除指定的对象, O(n)
  public boolean remove(Object obj) {
    Node<T> p = findByItem(obj);
    if (p == null) {
      return false;
    }
    remove(p);
    return true;
  }

  // 查找指定对象在链表中的索引, O(n)
  public int indexOf(Object obj) {
    Node<T> p = head;
    int pos = 0;
    if (obj == null) {
      while (p != null && p.data != null) {
        p = p.next;
        pos++;
      }
    } else {
      while (p != null && !obj.equals(p.data)) {
        p = p.next;
        pos++;
      }
    }
    return pos;
  }

  public int lastIndexOf(Object obj) {
    Node<T> p = tail;
    int pos = size - 1;
    if (obj == null) {
      while (p != null && p.data != null) {
        p = p.prev;
        pos--;
      }
    } else {
      while (p != null && !obj.equals(p.data)) {
        p = p.prev;
        pos--;
      }
    }
    return pos;
  }

  public T get(int index) {
    return findByIndex(index).data;
  }

  // 检查链表中是否包含某个值
  public boolean contains(Object obj) {
    return indexOf(obj) != -1;
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

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    Node<T> p = head;
    while (p != null) {
      sb.append(p.data);
      if (p.next != null) {
        sb.append(", ");
      }
      p = p.next;
    }
    sb.append(" ]");
    return sb.toString();
  }

  private void checkIndexForAdd(int index) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException(String.format("Index: %d, Size: ", index, size));
    }
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
    }
  }

  private Node<T> findByIndex(int index) {
    checkIndex(index);
    Node<T> p;
    int pos;
    if (index < (size << 1)) {
      p = head;
      pos = 0;
      while (p != null && pos != index) {
        p = p.next;
        pos++;
      }
    } else {
      p = tail;
      pos = size - 1;
      while (p != null && pos != index) {
        p = p.prev;
        pos--;
      }
    }
    return p;
  }

  private Node<T> findByItem(Object obj) {
    Node<T> p = head;
    if (obj == null) {
      while (p != null && p.data != null) {
        p = p.next;
      }
    } else {
      while (p != null && !obj.equals(p.data)) {
        p = p.next;
      }
    }
    return p;
  }

  private void addBefore(Node<T> node, T value) {
    if (isEmpty() || node == null) {
      return;
    }
    Node<T> p = tail;
    while (p != null && p != node) {
      p = p.prev;
    }
    if (p == null) {
      return;
    }
    if (p.prev == null) {
      addFirst(value);
      return;
    }
    Node<T> newNode = new Node<>(value, null, null);
    newNode.next = p;
    newNode.prev = p.prev;
    p.prev.next = newNode;
    p.prev = newNode;
    size++;
  }

  private void addAfter(Node<T> node, T value) {
    if (head == null || node == null) {
      return;
    }
    Node<T> p = head;
    while (p != null && p != node) {
      p = p.next;
    }
    if (p == null) {
      return;
    }
    if (p.next == null) {
      addLast(value);
      return;
    }
    Node<T> newLink = new Node<>(value, null, null);
    newLink.next = p.next;
    newLink.prev = p;
    p.next.prev = newLink;
    p.next = newLink;
    size++;
  }

  // 内部节点类
  private static class Node<T> {
    private T data; // 数据
    private Node<T> prev, next; // 前向和后向指针

    public Node(T data, Node<T> prev, Node<T> next) {
      this.data = data;
      this.prev = prev;
      this.next = next;
    }

    @Override
    public String toString() {
      if (data == null) {
        return "null";
      }
      return data.toString();
    }
  }
}
