package com.shawn.study.deep.in.java.design.behavioral.template;

public interface Sort {

  void sort(int[] a);

  default void show(Comparable[] a) {
    for (Comparable i : a) {
      System.out.print(i + " ");
    }
    System.out.println();
  }
}
