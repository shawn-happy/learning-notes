package com.shawn.study.datastructure.linkedlist;

/**
 * @author shawn
 * @since 2020/10/11
 */
public class CircleLinkedList<E> {
  private int size;

  private Node<E> head;

  public CircleLinkedList() {
    head = new Node<E>(null, head);
    size = 0;
  }

  public void append(E value) {
    if (value == null) {
      throw new NullPointerException("Cannot add null element to the list");
    }
    head.next = new Node<E>(value, head);
    size++;
  }

  public E remove(int pos) {
    if (pos > size || pos < 0) {
      throw new IndexOutOfBoundsException("position cannot be greater than size or negative");
    }
    Node<E> iterator = head.next;
    Node<E> before = head;
    for (int i = 1; i <= pos; i++) {
      iterator = iterator.next;
      before = before.next;
    }
    E saved = iterator.value;
    before.next = iterator.next;
    iterator.next = null;
    iterator.value = null;
    return saved;
  }

  private static class Node<E> {

    Node<E> next;
    E value;

    private Node(E value, Node<E> next) {
      this.value = value;
      this.next = next;
    }
  }
}
