package com.shawn.study.deep.in.java.jdbc.jpa.exception;

public class DeserializationException extends RuntimeException {

  public DeserializationException(String message) {
    super(message);
  }

  public DeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DeserializationException(Throwable cause) {
    super(cause);
  }
}
