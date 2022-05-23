package com.shawn.study.deep.in.spring.core.bean.definition;

import com.shawn.study.deep.in.spring.core.bean.definition.factory.DefaultUserFactory;
import com.shawn.study.deep.in.spring.core.bean.definition.factory.UserFactory;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

public class BeanInitializationAndDestroyAndGcDemo {

  @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
  public UserFactory userFactory() {
    return new DefaultUserFactory();
  }

  @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
  @Lazy
  public UserFactory userFactoryByLazy() {
    return new DefaultUserFactory();
  }

  public static void main(String[] args) throws Exception {
    initAndDestroyBean();
    System.out.println("lazy init & destroy...");
    initAndDestroyBeanByLazy();
  }

  public static void initAndDestroyBean() {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(BeanInitializationAndDestroyAndGcDemo.class);
    applicationContext.refresh();
    System.out.println("before init...");
    applicationContext.getBean("userFactory", DefaultUserFactory.class);
    System.out.println("after init...");
    System.out.println("before destroy");
    applicationContext.close();
    System.out.println("after destroy...");
  }

  public static void initAndDestroyBeanByLazy() throws Exception {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(BeanInitializationAndDestroyAndGcDemo.class);
    applicationContext.refresh();
    System.out.println("before init...");
    applicationContext.getBean("userFactoryByLazy", DefaultUserFactory.class);
    System.out.println("after init...");
    System.out.println("before destroy");
    applicationContext.close();
    System.out.println("after destroy...");
    TimeUnit.SECONDS.sleep(10);
    System.gc();
    TimeUnit.SECONDS.sleep(10);
  }
}
