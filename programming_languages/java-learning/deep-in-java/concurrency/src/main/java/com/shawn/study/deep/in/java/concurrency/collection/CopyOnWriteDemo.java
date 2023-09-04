package com.shawn.study.deep.in.java.concurrency.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteDemo {

  private static final int SIZE = 100000;

  public static void main(String[] args) {
    CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < SIZE; i++) {
      cowList.add(i);
    }
    System.out.println("CopyOnWriteArrayList. " + (System.currentTimeMillis() - startTime));
    startTime = System.currentTimeMillis();
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < SIZE; i++) {
      list.add(i);
    }
    System.out.println("ArrayList. " + (System.currentTimeMillis() - startTime));

    List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>());
    startTime = System.currentTimeMillis();
    for (int i = 0; i < SIZE; i++) {
      synchronizedList.add(i);
    }
    System.out.println("synchronizedList. " + (System.currentTimeMillis() - startTime));

    Vector<Integer> vector = new Vector<>();
    startTime = System.currentTimeMillis();
    for (int i = 0; i < SIZE; i++) {
      vector.add(i);
    }
    System.out.println("vector. " + (System.currentTimeMillis() - startTime));
  }
}
