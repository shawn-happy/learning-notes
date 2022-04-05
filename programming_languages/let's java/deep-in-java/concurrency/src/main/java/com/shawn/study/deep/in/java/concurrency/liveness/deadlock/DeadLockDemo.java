package com.shawn.study.deep.in.java.concurrency.liveness.deadlock;

import java.util.concurrent.TimeUnit;

public class DeadLockDemo {

  public static void main(String[] args) {
    final Object m1 = new Object();
    final Object m2 = new Object();

    new Thread(
            () -> {
              synchronized (m1) {
                System.out.printf("Thread[ ID : %d] holds m1\n", Thread.currentThread().getId());

                try {
                  TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ignored) {

                }

                synchronized (m2) {
                  System.out.printf("Thread[ ID : %d] holds m2\n", Thread.currentThread().getId());
                }
              }
            })
        .start();

    new Thread(
            () -> {
              synchronized (m2) {
                System.out.printf("Thread[ ID : %d] holds m2\n", Thread.currentThread().getId());

                try {
                  TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ignored) {

                }

                synchronized (m1) {
                  System.out.printf("Thread[ ID : %d] holds m1\n", Thread.currentThread().getId());
                }
              }
            })
        .start();
  }
}
