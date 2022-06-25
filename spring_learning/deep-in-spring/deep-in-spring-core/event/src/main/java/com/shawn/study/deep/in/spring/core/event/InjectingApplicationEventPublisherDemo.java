package com.shawn.study.deep.in.spring.core.event;

import javax.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class InjectingApplicationEventPublisherDemo
    implements ApplicationEventPublisherAware, ApplicationContextAware, BeanPostProcessor {

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private ApplicationContext applicationContext;

  @PostConstruct
  public void init() {
    // # 3
    applicationEventPublisher.publishEvent(
        new MySpringEvent("The event from @Autowired ApplicationEventPublisher"));
    // #4
    applicationContext.publishEvent(
        new MySpringEvent("The event from @Autowired ApplicationContext"));
  }

  public static void main(String[] args) {

    // 创建注解驱动 Spring 应用上下文
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    // 注册 Configuration Class
    context.register(InjectingApplicationEventPublisherDemo.class);

    // 增加 Spring 事件监听器
    context.addApplicationListener(new MySpringEventListener());

    // 启动 Spring 应用上下文
    context.refresh();

    // 关闭 Spring 应用上下文
    context.close();
  }

  @Override
  public void setApplicationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) { // #1
    applicationEventPublisher.publishEvent(
        new MySpringEvent("The event from ApplicationEventPublisherAware"));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException { // #2
    applicationEventPublisher.publishEvent(
        new MySpringEvent("The event from ApplicationContextAware"));
  }
}
