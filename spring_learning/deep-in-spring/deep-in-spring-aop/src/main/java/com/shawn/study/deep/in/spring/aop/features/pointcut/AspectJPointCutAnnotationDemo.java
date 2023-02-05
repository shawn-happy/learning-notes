package com.shawn.study.deep.in.spring.aop.features.pointcut;

import com.shawn.study.deep.in.spring.aop.features.config.AspectConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AspectJPointCutAnnotationDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(AspectJPointCutAnnotationDemo.class, AspectConfiguration.class);
    context.refresh();

    AspectJPointCutAnnotationDemo aspectJPointCutDemo =
        context.getBean(AspectJPointCutAnnotationDemo.class);
    aspectJPointCutDemo.execute();
    context.close();
  }

  public void execute() {
    System.out.println("AspectJPointCutDemo.execute");
  }
}
