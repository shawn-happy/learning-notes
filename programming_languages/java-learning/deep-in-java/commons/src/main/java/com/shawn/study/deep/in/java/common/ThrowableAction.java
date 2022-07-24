package com.shawn.study.deep.in.java.common;

import com.shawn.study.deep.in.java.common.exception.ExecutionException;
import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowableAction {

  void execute() throws Throwable;

  static void execute(ThrowableAction action) throws RuntimeException {
    execute(action, () -> new ExecutionException("execute error"));
  }

  static void execute(ThrowableAction action, Supplier<Throwable> supplier)
      throws RuntimeException {
    try {
      action.execute();
    } catch (Throwable e) {
      throw new ExecutionException(e.getMessage(), supplier.get());
    }
  }
}
