package com.shawn.study.deep.in.spring.core.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@EnableHelloWorld
public class EnableModuleDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    // 注册 Configuration Class
    context.register(EnableModuleDemo.class);

    // 启动 Spring 应用上下文
    context.refresh();

    String helloWorld = context.getBean("helloWorld", String.class);

    System.out.println(helloWorld);

    // 关闭 Spring 应用上下文
    context.close();
  }
}
