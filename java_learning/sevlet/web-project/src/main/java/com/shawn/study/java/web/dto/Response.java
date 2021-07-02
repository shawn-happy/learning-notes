package com.shawn.study.java.web.dto;

public class Response<T> {

  private int code;
  private String message;
  private T data;

  public Response() {}

  public Response(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  private static final int SUCCESS_STATUS = 0;
  private static final int FAIL = 1;

  public static <T> Response<T> ok(T data) {
    return new Response<>(SUCCESS_STATUS, null, data);
  }

  public static <T> Response<T> ok() {
    return new Response<>(SUCCESS_STATUS, null, null);
  }

  public static Response<Void> error(int status, String message) {
    return new Response<>(status, message, null);
  }

  public static <T> Response<T> error(String error) {
    return new Response<>(FAIL, error, null);
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
