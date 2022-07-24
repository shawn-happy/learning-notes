package com.shawn.study.deep.in.java.common.exception;

public class ExecutionException extends RuntimeException {

  public ExecutionException(String message) {
    super(message);
  }

  public ExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecutionException(Throwable cause) {
    super(cause);
  }
}
