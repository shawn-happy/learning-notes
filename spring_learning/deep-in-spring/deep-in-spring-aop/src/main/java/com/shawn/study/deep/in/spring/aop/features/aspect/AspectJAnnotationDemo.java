package com.shawn.study.deep.in.spring.aop.features.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect // 声明为 Aspect 切面
@Configuration
@EnableAspectJAutoProxy
public class AspectJAnnotationDemo {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(AspectJAnnotationDemo.class);
    context.refresh();

    AspectJAnnotationDemo aspectJAnnotationDemo = context.getBean(AspectJAnnotationDemo.class);

    context.close();
  }
}
