package com.shawn.study.deep.in.java.collection;

import java.util.*;

public class CollectionDemo {

  public static void main(String[] args) {
    testThrowUnsupportedOperationException();
    testThrowNullPointerException();
    testThrowIllegalArgumentException();

    testEquals();
  }

  private static void testEquals() {
    List<Integer> list1 = new ArrayList<>();
    list1.add(1);
    list1.add(2);
    List<Integer> list2 = new ArrayList<>();
    list2.add(1);
    list2.add(2);
    System.out.println(list1.equals(list2));
  }

  private static void testThrowIllegalArgumentException() {
    List<Integer> collection = new ArrayList<>();
    try {
      collection.subList(1, 0);
    } catch (IllegalArgumentException e) {
      System.out.println("IllegalArgumentException");
    }
  }

  private static void testThrowNullPointerException() {
    Collection<Integer> collection = new ArrayList<>();
    collection.add(1);
    Object[] array = null;
    try {
      collection.toArray(array);
    } catch (NullPointerException e) {
      System.out.println("NullPointerException");
    }
  }

  private static void testThrowUnsupportedOperationException() {
    Collection<Integer> collection =
        Collections.unmodifiableCollection(Arrays.asList(1, 3, 4, 5, 2, 7));
    try {
      collection.add(8);
    } catch (UnsupportedOperationException e) {
      System.out.println("unmodifiable collection");
    }
  }
}
