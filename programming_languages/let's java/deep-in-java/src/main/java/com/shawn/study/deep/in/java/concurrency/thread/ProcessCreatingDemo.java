package com.shawn.study.deep.in.java.concurrency.thread;

import java.io.IOException;

/**
 * java创建进程demo
 *
 * @author shawn
 */
public class ProcessCreatingDemo {

  public static void main(String[] args) throws IOException {
    Runtime runtime = Runtime.getRuntime();
    runtime.exec("calc");
  }
}
