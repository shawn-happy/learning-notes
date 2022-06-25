package com.shawn.study.deep.in.spring.core.event;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class HierarchicalSpringEventPropagateDemo {
  public static void main(String[] args) {
    // 1. 创建 parent Spring 应用上下文
    AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
    parentContext.setId("parent-context");
    // 注册 MyListener 到 parent Spring 应用上下文
    parentContext.register(MyListener.class);

    // 2. 创建 current Spring 应用上下文
    AnnotationConfigApplicationContext currentContext = new AnnotationConfigApplicationContext();
    currentContext.setId("current-context");
    // 3. current -> parent
    currentContext.setParent(parentContext);
    // 注册 MyListener 到 current Spring 应用上下文
    currentContext.register(MyListener.class);

    // 4.启动 parent Spring 应用上下文
    parentContext.refresh();

    // 5.启动 current Spring 应用上下文
    currentContext.refresh();

    // 关闭所有 Spring 应用上下文
    currentContext.close();
    parentContext.close();
  }

  static class MyListener implements ApplicationListener<ApplicationContextEvent> {

    private Set<ApplicationContextEvent> processedEvents = new LinkedHashSet<>();

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
      if (processedEvents.add(event)) {
        System.out.printf(
            "监听到 Spring 应用上下文[ ID : %s ] 事件 :%s\n",
            event.getApplicationContext().getId(), event.getClass().getSimpleName());
      }
    }
  }
}
