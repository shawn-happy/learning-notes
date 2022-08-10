package com.shawn.study.deep.in.java.design.behavioral.interpreter;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author shawn
 * @since 2020/8/13
 */
public class ExpressionInterpreter {

  private Deque<Expression> numbers = new LinkedList<>();

  public long interpreter(String expression) {
    String[] elements = expression.split(" ");
    int length = elements.length;
    for (int i = 0; i < (length + 1) / 2; i++) {
      numbers.addLast(new NumberExpression(elements[i]));
    }

    for (int i = (length + 1) / 2; i < length; i++) {
      String operator = elements[i];
      boolean isValid =
          "+".equals(operator)
              || "-".equals(operator)
              || "*".equals(operator)
              || "/".equals(operator);
      if (!isValid) {
        throw new RuntimeException("Expression is invalid: " + expression);
      }
      Expression expression1 = numbers.pollFirst();
      Expression expression2 = numbers.pollFirst();
      Expression combinedExp = null;
      if ("+".equals(operator)) {
        combinedExp = new AdditionExpression(expression1, expression2);
      } else if ("-".equals(operator)) {
        combinedExp = new SubstractionExpression(expression1, expression2);
      } else if ("*".equals(operator)) {
        combinedExp = new MultiplicationExpression(expression1, expression2);
      } else if ("/".equals(operator)) {
        combinedExp = new DivisionExpression(expression1, expression2);
      }
      long result = combinedExp.interpret();
      numbers.addFirst(new NumberExpression(result));
    }
    if (numbers.size() != 1) {
      throw new RuntimeException("Expression is invalid: " + expression);
    }
    return numbers.pop().interpret();
  }
}
