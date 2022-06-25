package com.shawn.study.deep.in.spring.core.environment;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

public class InjectingEnvironmentDemo implements EnvironmentAware, ApplicationContextAware {

  private Environment environment;

  @Autowired private Environment environment2;

  private ApplicationContext applicationContext;

  @Autowired private ApplicationContext applicationContext2;

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    // 注册 Configuration Class
    context.register(InjectingEnvironmentDemo.class);

    // 启动 Spring 应用上下文
    context.refresh();

    InjectingEnvironmentDemo injectingEnvironmentDemo =
        context.getBean(InjectingEnvironmentDemo.class);

    System.out.println(injectingEnvironmentDemo.environment);

    System.out.println(
        injectingEnvironmentDemo.environment == injectingEnvironmentDemo.environment2);

    System.out.println(injectingEnvironmentDemo.environment == context.getEnvironment());

    System.out.println(
        injectingEnvironmentDemo.environment
            == injectingEnvironmentDemo.applicationContext.getEnvironment());

    System.out.println(
        injectingEnvironmentDemo.applicationContext
            == injectingEnvironmentDemo.applicationContext2);

    System.out.println(injectingEnvironmentDemo.applicationContext == context);

    // 关闭 Spring 应用上下文
    context.close();
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
