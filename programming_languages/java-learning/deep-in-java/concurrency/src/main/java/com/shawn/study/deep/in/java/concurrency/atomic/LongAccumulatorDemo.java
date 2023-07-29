package com.shawn.study.deep.in.java.concurrency.atomic;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;

public class LongAccumulatorDemo {

  public static class LongMax implements LongBinaryOperator {

    @Override
    public long applyAsLong(long left, long right) {
      return Math.max(left, right);
    }
  }

  public static void main(String[] args) {
    LongAccumulator accumulator = new LongAccumulator(new LongMax(), Long.MIN_VALUE);
    accumulator.accumulate(10);
    accumulator.accumulate(-3);
    accumulator.accumulate(20);
    System.out.println(accumulator.get());
  }
}
