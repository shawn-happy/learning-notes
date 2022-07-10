package com.shawn.study.deep.in.java.concurrency.thread;

/**
 * 停止线程DEMO
 *
 * @author shawn
 */
public class ThreadStopDemo {

  public static void main(String[] args) throws Exception {
    stopByInterrupt();
    stopByFlag();
  }

  private static void stopByInterrupt() throws InterruptedException {
    System.out.println("stop by Interrupt");
    Thread t =
        new Thread(
            () -> {
              if (!Thread.currentThread().isInterrupted()) {
                action();
              }
            },
            "t2");
    t.start();
    t.interrupt();
    t.join();
  }

  private static void stopByFlag() throws InterruptedException {
    System.out.println("stop by flag");
    Action action = new Action();
    Thread t = new Thread(action, "t1");
    t.start();
    action.cancel();
    t.join();
  }

  private static class Action implements Runnable {

    private volatile boolean stopped = false;

    @Override
    public void run() {
      if (!stopped) {
        action();
      }
    }

    private void cancel() {
      stopped = true;
    }
  }

  private static void action() {
    System.out.printf("当前线程[%s], 正在执行。。。\n", Thread.currentThread().getName());
  }
}
