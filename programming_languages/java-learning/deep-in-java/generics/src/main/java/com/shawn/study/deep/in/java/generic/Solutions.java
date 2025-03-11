package com.shawn.study.deep.in.java.generic;

import java.util.Stack;

public class Solutions {

  public static void main(String[] args) {
    // [1, 2, 3]
    ListNode node = new ListNode(1, new ListNode(2, new ListNode(3, null)));
    new Solutions().reverse(node);
    System.out.println();
  }

  public ListNode reverse(ListNode node) {
    if (node == null) {
      return null;
    }
    ListNode p = node;
    ListNode prev = null;
    while (p != null) {
      int val = p.value;
      p = p.next;
      addFirst(val, prev);
    }
    return prev;
  }

  static class ListNode {
    public int value;
    public ListNode next;

    public ListNode(int value, ListNode next) {
      this.value = value;
      this.next = next;
    }

        @Override
        public String toString() {
          String val = String.valueOf(value);
          while (next != null) {
            val += "," + String.valueOf(next.value);
          }
          ;
          return val;
        }
  }

  public void addFirst(int value, ListNode node) {
    if (node == null) {
      node = new ListNode(value, null);
      return;
    }
    ListNode listNode = new ListNode(value, null);
    listNode.next = node;
    node = listNode;
  }
}
