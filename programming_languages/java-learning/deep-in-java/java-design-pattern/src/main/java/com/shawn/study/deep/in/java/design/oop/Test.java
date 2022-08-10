package com.shawn.study.deep.in.java.design.oop;

import java.util.Random;

public class Test {

  public static void main(String[] args) {
    List<IntegerComparable> list = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < 5; i++) {
      IntegerComparable value = new IntegerComparable();
      value.setValue(random.nextInt(100));
      list.add(value);
    }
    System.out.println(list.toString());
    list = new SortArrayList<>();
    for (int i = 0; i < 5; i++) {
      IntegerComparable value = new IntegerComparable();
      value.setValue(random.nextInt(100));
      list.add(value);
    }
    System.out.println(list.toString());
  }
}
