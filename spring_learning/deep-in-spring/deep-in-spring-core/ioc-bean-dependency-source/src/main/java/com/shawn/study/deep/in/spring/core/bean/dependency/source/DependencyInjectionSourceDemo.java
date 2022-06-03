package com.shawn.study.deep.in.spring.core.bean.dependency.source;

import com.shawn.study.deep.in.spring.core.ioc.domain.SuperUser;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import com.shawn.study.deep.in.spring.core.ioc.repository.UserRepository;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.DefaultEventListenerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/** 依赖注入来源示例 */
public class DependencyInjectionSourceDemo {

  @Autowired @Qualifier private SuperUser superUserBean;
  @Autowired private Environment environment;
  @Autowired private BeanFactory beanFactory;
  @Autowired private ResourceLoader resourceLoader;
  @Autowired private ApplicationContext applicationContext;
  @Autowired private ApplicationEventPublisher applicationEventPublisher;
  @Autowired private DefaultEventListenerFactory defaultEventListenerFactory;

  @Bean
  @Qualifier
  public SuperUser superUserBean() {
    SuperUser superUserBean = new SuperUser();
    superUserBean.setId("2");
    superUserBean.setName("Jack");
    superUserBean.setAge(27);
    superUserBean.setAddress("BeiJing");
    superUserBean.setIdCard("12345");
    return superUserBean;
  }

  // Spring内建BeanDefinition和SingletonBean依赖注入
  @PostConstruct
  public void initByInjection() {
    System.out.println("beanFactory == applicationContext " + (beanFactory == applicationContext));
    System.out.println(
        "beanFactory == applicationContext.getBeanFactory() "
            + (beanFactory == applicationContext.getAutowireCapableBeanFactory()));
    System.out.println(
        "resourceLoader == applicationContext " + (resourceLoader == applicationContext));
    System.out.println(
        "ApplicationEventPublisher == applicationContext "
            + (applicationEventPublisher == applicationContext));
    System.out.println(defaultEventListenerFactory);
    System.out.println(environment);
  }

  public static void main(String[] args) {
    // Spring BeanDefinition
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(DependencyInjectionSourceDemo.class);
    applicationContext.refresh();

    XmlBeanDefinitionReader xmlBeanDefinitionReader =
        new XmlBeanDefinitionReader(applicationContext);
    xmlBeanDefinitionReader.loadBeanDefinitions("classpath:/dependency-injection.xml");
    System.out.println("配置BeanDefinition作为依赖注入来源：");
    // xml
    UserRepository userRepository =
        applicationContext.getBean("userRepository", UserRepository.class);
    User[] users = userRepository.getUsers();
    System.out.println("xml依赖注入：");
    Arrays.stream(users).forEach(System.out::println);
    // annotation
    DependencyInjectionSourceDemo demo =
        applicationContext.getBean(DependencyInjectionSourceDemo.class);
    System.out.println("annotation依赖注入：" + demo.superUserBean);
    // BeanDefinitionBuilder
    AbstractBeanDefinition superUserRepositoryBeanDefinition =
        BeanDefinitionBuilder.genericBeanDefinition(UserRepository.class)
            .addPropertyReference("superUser", "superUser")
            .getBeanDefinition();
    applicationContext.registerBeanDefinition(
        "superUserRepositoryBeanDefinition", superUserRepositoryBeanDefinition);
    UserRepository superUserRepository =
        applicationContext.getBean("superUserRepositoryBeanDefinition", UserRepository.class);
    System.out.println("BeanDefinition依赖注入: " + superUserRepository.getSuperUser());

    // SingletonBean作为依赖注入来源
    User singletonUser = new User();
    singletonUser.setId("4");
    singletonUser.setAge(29);
    singletonUser.setName("Bob");
    singletonUser.setAddress("TianJing");
    SingletonBeanRegistry singletonBeanRegistry = applicationContext.getBeanFactory();
    // 注册外部单例对象
    singletonBeanRegistry.registerSingleton("userSingletonBean", singletonUser);
    AbstractBeanDefinition userRepositoryBeanDefinition =
        BeanDefinitionBuilder.genericBeanDefinition(UserRepository.class)
            .addPropertyReference("user", "userSingletonBean")
            .getBeanDefinition();
    applicationContext.registerBeanDefinition(
        "userRepositoryBeanDefinition", userRepositoryBeanDefinition);
    UserRepository userRepositorySingletonBean =
        applicationContext.getBean("userRepositoryBeanDefinition", UserRepository.class);
    System.out.println("SingletonBean对象依赖注入: " + userRepositorySingletonBean.getUser());

    applicationContext.close();
  }
}
