package com.shawn.study.deep.in.spring.core.application.context.lifecycle;

import org.springframework.context.Lifecycle;

public class MyLifecycle implements Lifecycle {

  private boolean running = false;

  @Override
  public void start() {
    running = true;
    System.out.println("MyLifecycle 启动...");
  }

  @Override
  public void stop() {
    running = false;
    System.out.println("MyLifecycle 停止...");
  }

  @Override
  public boolean isRunning() {
    return running;
  }
}
