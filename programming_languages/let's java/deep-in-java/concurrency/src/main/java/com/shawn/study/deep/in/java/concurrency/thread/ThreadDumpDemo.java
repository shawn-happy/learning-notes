package com.shawn.study.deep.in.java.concurrency.thread;

/** 线程dump信息 */
public class ThreadDumpDemo {

  public static void main(String[] args) {
    new Throwable("Stack Trace").printStackTrace(System.out);

    Thread.dumpStack();

    // Java 9 StackWalker API
    StackWalker stackWalker = StackWalker.getInstance();
    stackWalker.forEach(System.out::println);
  }
}
