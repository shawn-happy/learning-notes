package com.shawn.study.deep.in.java.jvm;

public class ExceptionDemo {

  public void execute() {
    int i = 0;
    try {
      i = 100 / 0;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      i = 1;
    }
  }
}
