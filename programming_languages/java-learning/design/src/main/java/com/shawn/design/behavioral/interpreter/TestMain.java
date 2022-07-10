package com.shawn.design.behavioral.interpreter;

/**
 * @author shawn
 * @since 2020/8/13
 */
public class TestMain {

  public static void main(String[] args) {
    ExpressionInterpreter interpreter = new ExpressionInterpreter();
    long result = interpreter.interpreter("8 3 2 4 - + *");
    System.out.println(result);
  }
}
