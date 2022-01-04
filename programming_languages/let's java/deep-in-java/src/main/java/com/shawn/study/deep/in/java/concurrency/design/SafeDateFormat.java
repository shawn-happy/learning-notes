package com.shawn.study.deep.in.java.concurrency.design;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

// threadLocal模式
public class SafeDateFormat {

  private static final ThreadLocal<DateFormat> t1 =
      ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

  static DateFormat get() {
    return t1.get();
  }
}
