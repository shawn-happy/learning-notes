package com.shawn.study.deep.in.java.jvm;

public class Test {

  public int test() {
    int i = 1;
    int sum = 0;
    while (i <= 10) {
      if (i == 8) {
        break;
      }
      sum += i;
      i++;
    }
    return sum;
  }

  public static void main(String[] args) {
    //		Test test = new Test();
    //		System.out.println(test.test());
    //		SuperClass[] arr = new SuperClass[10];
    System.out.println(Child.value);
  }
}
