package com.shawn.study.deep.in.java.jvm;

public class ClassLoaderDemo {

  public static void main(String[] args) {
    ClassLoader classLoader = ClassLoaderDemo.class.getClassLoader();
    System.out.println(classLoader.getName());
    ClassLoader parent = classLoader.getParent();
    System.out.println(parent.getName());
    ClassLoader parentParent = parent.getParent();
    System.out.println(parentParent == null);
  }
}
