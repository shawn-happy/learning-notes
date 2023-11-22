package com.shawn.study.deep.in.java.jvm;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class WeakReferenceDemo {
  public static void main(String[] args) throws Exception {
    Object obj = new Object();
    WeakReference<Object> weakReference = new WeakReference<>(obj);
    obj = null;
    System.gc();
    TimeUnit.SECONDS.sleep(200);
    System.out.println(weakReference.get());
    System.out.println(weakReference.isEnqueued());
  }
}
