package com.shawn.study.deep.in.java.concurrency.thread;

import com.sun.management.ThreadMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

/**
 * 线程监控示例
 *
 * @author shawn
 */
public class ThreadMonitorDemo {
  public static void main(String[] args) {
    ThreadMXBean bean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
    int threadCount = bean.getThreadCount();
    System.out.printf("当前jvm一共有[%d]个线程\n", threadCount);
    long[] threadIds = bean.getAllThreadIds();
    for (long id : threadIds) {
      ThreadInfo threadInfo = bean.getThreadInfo(id);
      System.out.println(threadInfo);
      long bs = bean.getThreadAllocatedBytes(id);
      System.out.printf("当前线程[%d, %s], 分配内存：%s KB\n", id, threadInfo.getThreadName(), bs / 1000.0);
    }
  }
}
