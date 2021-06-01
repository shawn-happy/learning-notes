package com.shawn.jvm;

/**
 * 演示java虚拟机栈Oom.
 */
public class JavaVmOOMDemo {

  public static void main(String[] args) {
    while (true){
      new Thread(() -> {
        while (true){
          System.out.println("11111");
        }
      }).start();
    }
  }

}
