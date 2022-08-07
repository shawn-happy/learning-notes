package com.shawn.study.deep.in.java.design.structural.flyweight;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public class IntegerTest {

  public static void main(String[] args) {
    //
    Integer i1 = 1;
    Integer i2 = 1;
    Integer i3 = new Integer(1);
    Integer i31 = Integer.valueOf(1);
    System.out.printf("i1 == i2?  result: %s\n", (i1 == i2));
    System.out.printf("i1 == i3?  result: %s\n", (i1 == i3));
    System.out.printf("i1 == i31?  result: %s\n", (i1 == i31));

    Integer i4 = 129;
    Integer i5 = 129;
    System.out.printf("i4 == i5?  result: %s\n", (i4 == i5));
  }
}
