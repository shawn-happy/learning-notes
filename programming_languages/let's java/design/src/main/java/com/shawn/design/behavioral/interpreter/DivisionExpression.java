package com.shawn.design.behavioral.interpreter;

/**
 * @author shawn
 * @since 2020/8/13
 */
public class DivisionExpression extends Expression {

  private Expression exp1;
  private Expression exp2;

  public DivisionExpression(Expression exp1, Expression exp2) {
    this.exp1 = exp1;
    this.exp2 = exp2;
  }

  @Override
  public long interpret() {
    if (exp2.interpret() == 0) {
      throw new ArithmeticException("divisor can not be zero");
    }
    return exp1.interpret() / exp2.interpret();
  }
}