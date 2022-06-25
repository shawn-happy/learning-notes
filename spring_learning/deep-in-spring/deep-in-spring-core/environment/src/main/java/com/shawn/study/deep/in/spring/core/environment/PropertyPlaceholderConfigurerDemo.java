package com.shawn.study.deep.in.spring.core.environment;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PropertyPlaceholderConfigurerDemo {

  public static void main(String[] args) {

    // 创建并且启动 Spring 应用上下文
    ClassPathXmlApplicationContext context =
        new ClassPathXmlApplicationContext("placeholders-resolver.xml");

    User user = context.getBean("user", User.class);

    System.out.println(user);

    // 关闭 Spring 应用上下文
    context.close();
  }
}
