package com.shawn.study.deep.in.java.concurrency.thread;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class Accumulator {

  private volatile int value;

  private static final Unsafe unsafe = Unsafe.getUnsafe();
  private static final long valueOffset;

  static {
    try {
      valueOffset = unsafe.objectFieldOffset(Accumulator.class.getDeclaredField("value"));
    } catch (Exception ex) {
      throw new Error(ex);
    }
  }

  public Accumulator() {}

  public Accumulator(int value) {
    this.value = value;
  }

  public int incrementAndGet() {
    return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
  }

  private static Unsafe reflectGetUnsafe() {
    try {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      return (Unsafe) field.get(null);
    } catch (Exception e) {
      throw new SecurityException(e);
    }
  }

  public static void main(String[] args) {
    Unsafe theUnsafe = reflectGetUnsafe();
    Unsafe unsafe = Unsafe.getUnsafe();
    System.out.println(theUnsafe == Accumulator.unsafe);
    System.out.println(unsafe);
  }
}
