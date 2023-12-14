package com.shawn.study.deep.in.java.jvm;

/**
 * VM Args: -Xss128k test stack over flow
 *
 * @author shawn
 */
public class StackOverFlowDemo {

  private int stackLength = 1;

  public void recursion() {
    stackLength++;
    recursion();
  }

  public static void main(String[] args) {
    StackOverFlowDemo demo = new StackOverFlowDemo();
    try {
      demo.recursion();
    } catch (Throwable e) {
      System.out.println("current stack depth: " + demo.stackLength);
      throw e;
    }
  }
}
