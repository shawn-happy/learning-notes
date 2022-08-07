package com.shawn.study.deep.in.java.design.behavioral.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shawn
 * @since 2020/8/4
 */
public class StrategyFactory {

  private static final Map<String, Strategy> strategies = new HashMap<>();

  static {
    strategies.put("A", new ConcreteStrategyA());
    strategies.put("B", new ConcreteStrategyB());
  }

  public static Strategy getStrategy(String type) {
    if (null == type || type.isEmpty()) {
      throw new IllegalArgumentException("type should not be empty.");
    }
    return strategies.get(type);
  }
}
