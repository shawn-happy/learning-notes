package com.shawn.study.deep.in.java.jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceDemo {
  public static void main(String[] args) {
    Object obj = new Object();
    ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
    PhantomReference<Object> phantomReference = new PhantomReference<>(obj, refQueue);
    System.out.println(phantomReference.get());
    System.out.println(phantomReference.isEnqueued());
  }
}
