package com.shawn.study.deep.in.spring.core.environment;

import static org.springframework.context.ConfigurableApplicationContext.ENVIRONMENT_BEAN_NAME;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

public class LookupEnvironmentDemo implements EnvironmentAware {

  private Environment environment;

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    // 注册 Configuration Class
    context.register(LookupEnvironmentDemo.class);

    // 启动 Spring 应用上下文
    context.refresh();

    LookupEnvironmentDemo lookupEnvironmentDemo = context.getBean(LookupEnvironmentDemo.class);

    // 通过 Environment Bean 名称 依赖查找
    Environment environment = context.getBean(ENVIRONMENT_BEAN_NAME, Environment.class);

    System.out.println(lookupEnvironmentDemo.environment);

    System.out.println(lookupEnvironmentDemo.environment == environment);

    // 关闭 Spring 应用上下文
    context.close();
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
