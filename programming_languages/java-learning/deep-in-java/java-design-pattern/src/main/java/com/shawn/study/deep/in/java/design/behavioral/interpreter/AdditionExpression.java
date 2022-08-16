package com.shawn.study.deep.in.java.design.behavioral.interpreter;

/**
 * @author shawn
 * @since 2020/8/13
 */
public class AdditionExpression extends Expression {

  private Expression exp1;
  private Expression exp2;

  public AdditionExpression(Expression exp1, Expression exp2) {
    this.exp1 = exp1;
    this.exp2 = exp2;
  }

  @Override
  public long interpret() {
    return exp1.interpret() + exp2.interpret();
  }
}