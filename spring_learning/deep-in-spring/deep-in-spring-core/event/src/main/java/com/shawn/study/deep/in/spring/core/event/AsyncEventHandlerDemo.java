package com.shawn.study.deep.in.spring.core.event;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.concurrent.ExecutorService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public class AsyncEventHandlerDemo {

  public static void main(String[] args) {

    GenericApplicationContext context = new GenericApplicationContext();

    // 1.添加自定义 Spring 事件监听器
    context.addApplicationListener(new MySpringEventListener());

    // 2.启动 Spring 应用上下文
    context.refresh(); // 初始化 ApplicationEventMulticaster

    // 依赖查找 ApplicationEventMulticaster
    ApplicationEventMulticaster applicationEventMulticaster =
        context.getBean(
            AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
            ApplicationEventMulticaster.class);

    // 判断当前 ApplicationEventMulticaster 是否为 SimpleApplicationEventMulticaster
    if (applicationEventMulticaster instanceof SimpleApplicationEventMulticaster) {
      SimpleApplicationEventMulticaster simpleApplicationEventMulticaster =
          (SimpleApplicationEventMulticaster) applicationEventMulticaster;
      // 切换 taskExecutor
      ExecutorService taskExecutor =
          newSingleThreadExecutor(new CustomizableThreadFactory("my-spring-event-thread-pool"));
      // 同步 -> 异步
      simpleApplicationEventMulticaster.setTaskExecutor(taskExecutor);

      // 添加 ContextClosedEvent 事件处理
      applicationEventMulticaster.addApplicationListener(
          new ApplicationListener<ContextClosedEvent>() {
            @Override
            public void onApplicationEvent(ContextClosedEvent event) {
              if (!taskExecutor.isShutdown()) {
                taskExecutor.shutdown();
              }
            }
          });

      simpleApplicationEventMulticaster.setErrorHandler(
          e -> {
            System.err.println("当 Spring 事件异常时，原因：" + e.getMessage());
          });
    }

    context.addApplicationListener(
        new ApplicationListener<MySpringEvent>() {
          @Override
          public void onApplicationEvent(MySpringEvent event) {
            throw new RuntimeException("故意抛出异常");
          }
        });

    // 3. 发布自定义 Spring 事件
    context.publishEvent(new MySpringEvent("Hello,World"));

    // 4. 关闭 Spring 应用上下文（ContextClosedEvent）
    context.close();
  }
}
