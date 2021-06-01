package com.shawn.jvm;

public class StringInternDemo {

  public static void main(String[] args) {
    String str = new StringBuilder("计算机").append("软件").toString();
    System.out.println("str.intern() == str: " + (str.intern() == str));

    String java = "虚拟机";
    String str2 = new StringBuilder("虚拟").append("机").toString();
    System.out.println("java.intern() == str2: " + (java.intern() == str2));
  }

}
