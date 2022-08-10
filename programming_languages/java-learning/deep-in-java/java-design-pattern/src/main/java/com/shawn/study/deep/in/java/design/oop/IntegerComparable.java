package com.shawn.study.deep.in.java.design.oop;

/** @author shawn */
public class IntegerComparable implements Comparable<Integer> {

  private Integer value;

  public IntegerComparable() {}

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  @Override
  public int compareTo(Integer o) {
    return (this.value - o);
  }

  @Override
  public String toString() {
    return "" + value;
  }
}
