package com.shawn.study.deep.in.java.design.behavioral.interpreter;

/**
 * @author shawn
 * @since 2020/8/13
 */
public class NumberExpression extends Expression {

  private long number;

  public NumberExpression(long number) {
    this.number = number;
  }

  public NumberExpression(String number) {
    this.number = Long.valueOf(number);
  }

  @Override
  public long interpret() {
    return number;
  }
}
