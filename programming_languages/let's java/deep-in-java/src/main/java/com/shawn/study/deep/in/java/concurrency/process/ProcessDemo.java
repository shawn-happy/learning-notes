package com.shawn.study.deep.in.java.concurrency.process;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.Test;

public class ProcessDemo {

  @Test
  public void getPIDBeforeJava9() {
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    String name = runtimeMXBean.getName();
    String pid = name.substring(0, name.indexOf("@"));
    System.out.printf("使用java9之前的方法来获取当前进程的id: [%s]\n", pid);
  }

  @Test
  public void getPIDInJava9() {
    long pid = ProcessHandle.current().pid();
    System.out.printf("使用java9的方法来获取当前进程的id: [%d]\n", pid);
  }

  @Test
  public void getPIDAfterJava9() {
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    long pid = runtimeMXBean.getPid();
    System.out.printf("使用java9之后的方法来获取当前进程的id: [%d]\n", pid);
  }

  @Test
  public void getProcessInfo() {
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    long pid = runtimeMXBean.getPid();
    System.out.printf("使用java9之后的方法来获取当前进程的id: [%d]\n", pid);

    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

    Instant instant = Instant.ofEpochMilli(runtimeMXBean.getStartTime());
    LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.systemDefault());
    System.out.printf("当前jvm启动的时间：[%s]\n", localDate);
    System.out.printf("当前jvm上线的时间：[%d]\n", runtimeMXBean.getUptime());
    System.out.printf("当前jvm线程的数量：[%d]\n", threadMXBean.getThreadCount());
    System.out.printf("当前jvm运行的系统：[%s]\n", operatingSystemMXBean.getName());

    String vmName = runtimeMXBean.getVmName();
    System.out.printf("当前jvm运行的虚拟机名称：[%s]\n", vmName);
  }

  @Test
  public void startChildProcess() throws Exception {
    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    String name = operatingSystemMXBean.getName();
    if (name.startsWith("Mac")) {
      Process exec = Runtime.getRuntime().exec("open -a /System/Applications/Calculator.app");
    }
  }
}
