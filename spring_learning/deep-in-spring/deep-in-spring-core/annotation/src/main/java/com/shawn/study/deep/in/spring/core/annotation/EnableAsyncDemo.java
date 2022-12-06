package com.shawn.study.deep.in.spring.core.annotation;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
public class EnableAsyncDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(EnableAsyncDemo.class);
    applicationContext.refresh();

    EnableAsyncDemo demo = applicationContext.getBean(EnableAsyncDemo.class);
    System.out.println("sync1");
    System.out.println("sync2");
    demo.async();
    System.out.println("sync2");
    System.out.println("sync3");

    applicationContext.close();
  }

  @Async
  public void async() {
    try {
      TimeUnit.SECONDS.sleep(1);
      System.out.println(">>>>>>>>>>>>>>>>>>>");
      TimeUnit.SECONDS.sleep(1);
      System.out.println("===================");
      TimeUnit.SECONDS.sleep(1);
      System.out.println("<<<<<<<<<<<<<<<<<<<");
    } catch (Exception e) {

    }
  }

  public void sync() {
    System.out.println("sync1");
    System.out.println("sync2");
    async();
    System.out.println("sync3");
    System.out.println("sync4");
  }
}
