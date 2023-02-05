package com.shawn.study.deep.in.spring.aop.features.proxyFactory;

import com.shawn.study.deep.in.spring.aop.service.EchoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProxyFactoryBeanDemo {

  public static void main(String[] args) {
    ClassPathXmlApplicationContext context =
        new ClassPathXmlApplicationContext("classpath:spring-aop-context.xml");
    EchoService echoService = context.getBean("echoServiceProxyFactoryBean", EchoService.class);
    System.out.println(echoService.echo("proxy factory bean demo"));
    context.close();
  }
}
