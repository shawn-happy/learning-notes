package com.shawn.study.deep.in.spring.core.application.context.lifecycle;

import static org.springframework.context.support.LiveBeansView.MBEAN_DOMAIN_PROPERTY_NAME;

import java.io.IOException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LiveBeansViewDemo {

  public static void main(String[] args) throws IOException {

    // 添加 LiveBeansView 的 ObjectName 的 domain
    System.setProperty(MBEAN_DOMAIN_PROPERTY_NAME, "com.shawn.study.deep.in.spring.core");

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    // 注册 Configuration Class
    context.register(LiveBeansViewDemo.class);

    // 启动 Spring 应用上下文
    context.refresh();

    System.out.println("按任意键继续...");
    System.in.read();

    context.close();
    // 关闭 Spring 应用上下文
  }
}
