package com.shawn.study.deep.in.java.jvm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

public class SoftReferenceDemo {

  public static void main(String[] args) throws Exception{
    Object o = new Object();
    ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    SoftReference<Object> objSoftRef = new SoftReference<>(o, referenceQueue);
    System.out.println(objSoftRef.get());
    o = null;
    TimeUnit.SECONDS.sleep(200);
    System.gc();
    System.out.println(objSoftRef.get());
  }
}
