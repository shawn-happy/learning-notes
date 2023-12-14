package com.shawn.study.deep.in.java.jvm;

import java.util.concurrent.TimeUnit;

/** 复活即将被回收的对象DEMO */
public class FinalizeObjectSurvivorDemo {

  private static FinalizeObjectSurvivorDemo HOOK = null;

  public void isAlive() {
    System.out.println("yes, i'm still alive...");
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    System.out.println("finalize method executed!");
    HOOK = this;
  }

  public static void main(String[] args) throws Exception {
    HOOK = new FinalizeObjectSurvivorDemo();
    HOOK = null;
    // 对象第一次复活
    System.gc();
    // Finalizer Thread是一个低调度优先级的线程，需要暂停1S，确保finalize()方法的执行
    TimeUnit.SECONDS.sleep(1);
    if (HOOK != null) {
      HOOK.isAlive();
    } else {
      System.out.println("no, i'm dead...");
    }

    HOOK = null;
    // 对象被回收了， 因为同一个对象的finalize()只会被虚拟机执行一次
    System.gc();
    // Finalizer Thread是一个低调度优先级的线程，需要暂停1S，确保finalize()方法的执行
    TimeUnit.SECONDS.sleep(1);
    if (HOOK != null) {
      HOOK.isAlive();
    } else {
      System.out.println("no, i'm dead...");
    }
  }
}
