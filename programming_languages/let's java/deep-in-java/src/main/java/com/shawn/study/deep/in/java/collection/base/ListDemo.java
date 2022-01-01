package com.shawn.study.deep.in.java.collection.base;

import java.util.*;

public class ListDemo {

  public static void main(String[] args) {
    testOrderList();
    testDuplicateElement();
    testAddNullElement();
    testListIterator();
    testSearchMethod();
    testAdd();
    testRemove();
    testSet();
    testUnmodifiableLists();
  }

  private static void testUnmodifiableLists() {
    // 1. Collections.unmodifiableList
    List<User> strList = new ArrayList<>();
    strList.add(new User(1, "shawn1"));
    strList.add(new User(2, "shawn2"));
    strList.add(new User(3, "shawn3"));
    strList.add(new User(4, "shawn4"));
    strList.add(new User(5, "shawn5"));
    List<User> unmodifiableList = Collections.unmodifiableList(strList);
    try {
      User user = unmodifiableList.get(1);
      user.setName("jack");
      System.out.println(unmodifiableList.get(1).getName());
      unmodifiableList.add(new User(6, "shawn6"));
    } catch (Exception e) {
      System.out.println("unmodifiable List");
    }
    // 2. List.of()
    List<User> list = List.of(new User(1, "shawn1"), new User(2, "shawn2"), new User(3, "shawn3"));
    try {
      User user = unmodifiableList.get(1);
      user.setName("jack");
      System.out.println(list.get(1).getName());
      list.add(new User(4, "shawn4"));
    } catch (Exception e) {
      System.out.println("unmodifiable List");
    }

    // 3. List.of(), disallow null elements
    try {
      List.of(null);
    } catch (Exception e) {
      System.out.println("disallow null elements");
    }
  }

  private static void testSet() {
    List<String> strList = new ArrayList<>();
    strList.add("java");
    strList.add("go");
    strList.add("python");
    strList.add("c++");
    strList.add("html");
    strList.set(4, "javascript");
    for (String str : strList) {
      System.out.print(str + " ");
    }
    System.out.println();
  }

  private static void testRemove() {
    List<String> strList = new ArrayList<>();
    strList.add("java");
    strList.add("go");
    strList.add("python");
    strList.add("c++");
    strList.add("html");
    strList.remove("c++");
    strList.remove(1);
    for (String str : strList) {
      System.out.print(str + " ");
    }
    System.out.println();
  }

  private static void testAdd() {
    List<String> strList = new ArrayList<>(5);
    strList.add("java");
    strList.add(1, "python");
    strList.add(1, "html");
    for (String str : strList) {
      System.out.print(str + " ");
    }
    System.out.println();
  }

  private static void testSearchMethod() {
    List<String> strList = new ArrayList<>();
    strList.add("java");
    strList.add("go");
    strList.add("python");
    strList.add("c++");
    strList.add("html");
    System.out.println(strList.indexOf("c++"));
    System.out.println(strList.lastIndexOf("go"));
  }

  private static void testListIterator() {
    List<String> strList = new ArrayList<>();
    strList.add("java");
    strList.add("go");
    strList.add("python");
    strList.add("c++");
    strList.add("html");
    ListIterator<String> listIterator = strList.listIterator();
    while (listIterator.hasNext()) {
      String next = listIterator.next();
      System.out.print(next + " ");
    }
    System.out.println();

    while (listIterator.hasPrevious()) {
      String previous = listIterator.previous();
      System.out.print(previous + " ");
    }
    System.out.println();

    ListIterator<String> iterator = strList.listIterator(2);
    while (iterator.hasNext()) {
      String next = iterator.next();
      System.out.print(next + "");
    }
    System.out.println();
  }

  private static void testAddNullElement() {
    List<String> strList = new ArrayList<>();
    strList.add(null);
    strList.add("1");
    System.out.println(strList.get(0) == null);
    System.out.println(strList.get(1));
  }

  private static void testDuplicateElement() {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(1);
    System.out.println(list.get(0).equals(list.get(1)));
  }

  private static void testOrderList() {
    List<Integer> list = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      list.add(i);
    }
    System.out.println(list.get(0) == 1);
    System.out.println(list.get(1) == 2);
    System.out.println(list.get(2) == 3);

    List<Integer> linkedList = new LinkedList<>();
    for (int i = 1; i <= 3; i++) {
      linkedList.add(i);
    }
    System.out.println(linkedList.get(0) == 1);
    System.out.println(linkedList.get(1) == 2);
    System.out.println(linkedList.get(2) == 3);
  }
}

class User {
  private int id;
  private String name;

  public User(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
