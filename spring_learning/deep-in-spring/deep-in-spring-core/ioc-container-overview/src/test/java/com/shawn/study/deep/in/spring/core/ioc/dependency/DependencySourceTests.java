package com.shawn.study.deep.in.spring.core.ioc.dependency;

import static com.shawn.study.deep.in.spring.core.AssertUser.assertUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.shawn.study.deep.in.spring.core.AssertUser;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import com.shawn.study.deep.in.spring.core.ioc.repository.UserRepository;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class DependencySourceTests {

  private static DependencySourceDemo source;

  @BeforeClass
  public static void init() {
    source = new DependencySourceDemo();
  }

  /** 依赖来源一：自定义bean */
  @Test
  public void getDependencyFromCustom() {
    System.out.println("手动注入依赖：");
    UserRepository userRepository = source.getBeanFromXMl();
    Arrays.stream(userRepository.getUsers()).forEach(AssertUser::assertUser);

    System.out.println("自动注入依赖");
    UserRepository beanAutoWiring = source.getBeanAutoWiring();
    Arrays.stream(beanAutoWiring.getUsers()).forEach(AssertUser::assertUser);
  }

  /** 依赖来源二：内建依赖，非bean对象 */
  @Test
  public void getBuildInDependency() {
    UserRepository userRepository = source.getBeanFromXMl();
    BeanFactory beanFactory = userRepository.getBeanFactory();
    ObjectFactory<User> userObjectFactory = userRepository.getUserObjectFactory();
    ObjectFactory<ApplicationContext> objectFactory = userRepository.getObjectFactory();
    System.out.println("手动注入依赖，需要手动引入内建的依赖，如果不手动引入，则不会被注入进来");
    assertNull(beanFactory);
    assertNull(userObjectFactory);
    assertNull(objectFactory);

    System.out.println("自动注入依赖：");
    UserRepository beanAutoWiring = source.getBeanAutoWiring();
    beanFactory = beanAutoWiring.getBeanFactory();
    objectFactory = beanAutoWiring.getObjectFactory();
    userObjectFactory = beanAutoWiring.getUserObjectFactory();
    assertNotNull(beanFactory);
    assertNotNull(objectFactory);
    assertNotNull(userObjectFactory);
    BeanFactory applicationContext = source.getApplicationContext();
    assertNotEquals(applicationContext, beanFactory);

    User user = userObjectFactory.getObject();
    assertUser(user);
    ApplicationContext applicationContext2 = objectFactory.getObject();
    assertNotNull(applicationContext2);
    assertEquals(applicationContext, applicationContext2);

    System.out.println("UserRepository.getBeanFactory(): " + beanFactory);
    System.out.println(
        "ApplicationContext From DependencySourceDemo.getApplicationContext: "
            + applicationContext);
    System.out.println(
        "ApplicationContext From ObjectFactory<ApplicationContext>.getObject: "
            + applicationContext2);
  }

  /** 依赖来源三：Spring内建的Bean对象 */
  @Test
  public void getBuildInBeans() {
    UserRepository beanAutoWiring = source.getBeanAutoWiring();
    BeanFactory beanFactory = beanAutoWiring.getBeanFactory();
    if (beanFactory instanceof DefaultListableBeanFactory) {
      DefaultListableBeanFactory defaultListableBeanFactory =
          (DefaultListableBeanFactory) beanFactory;
      String[] beanDefinitionNames = defaultListableBeanFactory.getBeanDefinitionNames();
      Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    }
    Environment env1 = beanFactory.getBean("environment", Environment.class);
    Environment env2 = beanAutoWiring.getEnvironment();
    assertEquals(env1, env2);

    UserRepository userRepository = source.getBeanFromXMl();
    assertNull(userRepository.getEnvironment());
  }
}
