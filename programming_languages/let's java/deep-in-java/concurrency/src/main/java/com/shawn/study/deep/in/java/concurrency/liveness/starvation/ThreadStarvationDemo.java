package com.shawn.study.deep.in.java.concurrency.liveness.starvation;

import java.util.ArrayList;
import java.util.List;

public class ThreadStarvationDemo {

  public static void main(String[] args) {
    List<ThreadStarvationDemo> list = new ArrayList<>(1000);
    for (int i = 0; i < 1000; i++) {
      list.add(new ThreadStarvationDemo());
    }
    System.gc();
  }

  public void finalize() { // FinalReference
    System.out.printf(
        "Thread[%s] executes current object's finalization.\n", Thread.currentThread().getName());
  }
}
