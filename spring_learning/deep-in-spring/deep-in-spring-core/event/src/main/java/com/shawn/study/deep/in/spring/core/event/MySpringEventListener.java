package com.shawn.study.deep.in.spring.core.event;

import org.springframework.context.ApplicationListener;

public class MySpringEventListener implements ApplicationListener<MySpringEvent> {

  @Override
  public void onApplicationEvent(MySpringEvent event) {
    System.out.printf("[线程 ： %s] 监听到事件 : %s\n", Thread.currentThread().getName(), event);
  }
}
