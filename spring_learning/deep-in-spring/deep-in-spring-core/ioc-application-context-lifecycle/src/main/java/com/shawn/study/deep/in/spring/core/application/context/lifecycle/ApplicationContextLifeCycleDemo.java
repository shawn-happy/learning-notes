package com.shawn.study.deep.in.spring.core.application.context.lifecycle;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

import org.springframework.context.support.GenericApplicationContext;

public class ApplicationContextLifeCycleDemo {

  public static void main(String[] args) {
    GenericApplicationContext context = new GenericApplicationContext();
    // 注解 MyLifecycle 成为一个 Spring Bean
    context.registerBeanDefinition(
        "myLifecycle", rootBeanDefinition(MyLifecycle.class).getBeanDefinition());

    // 刷新 Spring 应用上下文
    context.refresh();

    // 启动 Spring 应用上下文
    context.start();

    // 停止 Spring 应用上下文
    context.stop();

    // 关闭 Spring 应用
    context.close();
  }
}
