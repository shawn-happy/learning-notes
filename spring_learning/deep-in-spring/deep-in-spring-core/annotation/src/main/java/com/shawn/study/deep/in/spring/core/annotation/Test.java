package com.shawn.study.deep.in.spring.core.annotation;

@MyComponent2
public class Test {

  private final String text = "my component";

  @Override
  public String toString() {
    return "Test{" + "text='" + text + '\'' + '}';
  }
}
