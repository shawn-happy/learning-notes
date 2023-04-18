package com.shawn.study.deep.in.java.jvm;

public class Demo {

  private String s;

  public Demo() {
    this.s = "hello world";
  }

  public Demo(String s) {
    this.s = s;
  }

  public void foo() {
    System.out.println(s);
  }

  public static void main(String[] args) {
    Demo demo = new Demo("shawn");
    demo.foo();
  }
}
