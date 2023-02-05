package com.shawn.study.deep.in.spring.aop.features.pointcut;

import com.shawn.study.deep.in.spring.aop.service.EchoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AspectJPointCutXmlDemo {

  public static void main(String[] args) {
    ClassPathXmlApplicationContext context =
        new ClassPathXmlApplicationContext("classpath:spring-aop-context.xml");
    EchoService echoService = context.getBean("echoService", EchoService.class);
    echoService.echo("aspect xml");
    echoService.display("aspect xml");
    context.close();
  }
}
