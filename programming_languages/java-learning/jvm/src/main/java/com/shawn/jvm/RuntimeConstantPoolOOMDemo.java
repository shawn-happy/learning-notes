package com.shawn.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * vm -Args -Xms10m -Xmx10m -XX:MaxDirectMemorySize=5m -XX:+PrintGCDetails
 *
 * @author shawn
 */
public class RuntimeConstantPoolOOMDemo {

  static String base = "string";

  public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      String str = base + base;
      base = str;
      list.add(str.intern());
    }
  }
}
