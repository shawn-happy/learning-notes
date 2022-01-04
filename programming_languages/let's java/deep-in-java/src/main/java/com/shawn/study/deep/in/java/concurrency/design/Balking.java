package com.shawn.study.deep.in.java.concurrency.design;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Balking {

  private static final Logger LOGGER = Logger.getLogger(Balking.class.getName());

  public static void main(String... args) {
    final WashingMachine washingMachine = new WashingMachine();
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    for (int i = 0; i < 3; i++) {
      executorService.execute(washingMachine::wash);
    }
    executorService.shutdown();
    try {
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException ie) {
      LOGGER.info("ERROR: Waiting on executor service shutdown!");
    }
  }
}

enum WashingMachineState {
  ENABLED,
  WASHING
}

class WashingMachine {

  private static final Logger LOGGER = Logger.getLogger(WashingMachine.class.getName());
  private final DelayProvider delayProvider;
  private WashingMachineState washingMachineState;

  /** Creates a new instance of WashingMachine */
  public WashingMachine() {
    this(
        (interval, timeUnit, task) -> {
          try {
            Thread.sleep(timeUnit.toMillis(interval));
          } catch (InterruptedException ie) {
            ie.printStackTrace();
          }
          task.run();
        });
  }

  /**
   * Creates a new instance of WashingMachine using provided delayProvider. This constructor is used
   * only for unit testing purposes.
   */
  public WashingMachine(DelayProvider delayProvider) {
    this.delayProvider = delayProvider;
    this.washingMachineState = WashingMachineState.ENABLED;
  }

  public WashingMachineState getWashingMachineState() {
    return washingMachineState;
  }

  /** Method responsible for washing if the object is in appropriate state */
  public void wash() {
    synchronized (this) {
      LOGGER.info(
          Thread.currentThread().getName() + ": Actual machine state: " + getWashingMachineState());
      if (washingMachineState == WashingMachineState.WASHING) {
        LOGGER.log(Level.SEVERE, "ERROR: Cannot wash if the machine has been already washing!");
        return;
      }
      washingMachineState = WashingMachineState.WASHING;
    }
    LOGGER.info(Thread.currentThread().getName() + ": Doing the washing");

    this.delayProvider.executeAfterDelay(50, TimeUnit.MILLISECONDS, this::endOfWashing);
  }

  /** Method responsible of ending the washing by changing machine state */
  public synchronized void endOfWashing() {
    washingMachineState = WashingMachineState.ENABLED;
    LOGGER.info(Thread.currentThread().getId() + ": Washing completed.");
  }
}

interface DelayProvider {

  void executeAfterDelay(long interval, TimeUnit timeUnit, Runnable task);
}

class AutoSaveEditor {

  // 文件是否被修改过
  boolean changed = false;
  // 定时任务
  ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

  // 定时执行自动保存
  void startAutoSave() {
    service.scheduleWithFixedDelay(
        () -> {
          autoSave();
        },
        5,
        5,
        TimeUnit.SECONDS);
  }

  void autoSave() {
    synchronized (this) {
      if (!changed) {
        return;
      }
      changed = false;
    }
    // 执行存盘操作
    // 省略且实现
  }

  void edit() {
    change();
  }

  void change() {
    synchronized (this) {
      changed = true;
    }
  }
}
