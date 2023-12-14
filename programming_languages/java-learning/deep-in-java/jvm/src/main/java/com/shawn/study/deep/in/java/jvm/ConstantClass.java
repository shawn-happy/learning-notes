package com.shawn.study.deep.in.java.jvm;

import java.util.UUID;

public class ConstantClass {

  public static final String STR = UUID.randomUUID().toString();

  static {
    System.out.println("constant class");
  }
}
