package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.source;

import com.shawn.study.deep.in.spring.core.ioc.domain.SuperUser;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.DefaultEventListenerFactory;

/**
 * 依赖查找来源示例:
 *
 * <ul>
 *   <li>来源一：配置BeanDefinition
 *   <li>来源二：注册SingletonBean
 *   <li>来源三：Spring内置可以查找的BeanDefinition
 *   <li>来源四：Spring内置可以查找的SingletonBean
 * </ul>
 *
 * @author Shawn
 */
public class DependencyLookupSourceDemo {

  @Bean
  public User superUserBean() {
    User superUserBean = new SuperUser();
    superUserBean.setId("2");
    superUserBean.setName("Jack");
    superUserBean.setAge(27);
    superUserBean.setAddress("BeiJing");
    return superUserBean;
  }

  public static void main(String[] args) {
    // Spring BeanDefinition
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(DependencyLookupSourceDemo.class);
    applicationContext.refresh();

    XmlBeanDefinitionReader xmlBeanDefinitionReader =
        new XmlBeanDefinitionReader(applicationContext);
    xmlBeanDefinitionReader.loadBeanDefinitions("classpath:/dependency-lookup.xml");
    // xml
    User user = applicationContext.getBean("user", User.class);
    System.out.println(user);
    // annotation
    SuperUser superUserBean = applicationContext.getBean("superUserBean", SuperUser.class);
    System.out.println(superUserBean);
    // BeanDefinitionBuilder
    AbstractBeanDefinition beanDefinition =
        BeanDefinitionBuilder.genericBeanDefinition(User.class)
            .addPropertyValue("id", "3")
            .addPropertyValue("name", "John")
            .addPropertyValue("age", 28)
            .getBeanDefinition();
    applicationContext.registerBeanDefinition("userBeanDefinition", beanDefinition);
    User userBeanDefinition = applicationContext.getBean("userBeanDefinition", User.class);
    System.out.println(userBeanDefinition);

    // 依赖查找SingletonBean
    // 创建一个外部 UserFactory 对象
    User singletonUser = new User();
    singletonUser.setId("4");
    singletonUser.setAge(29);
    singletonUser.setName("Bob");
    singletonUser.setAddress("TianJing");
    SingletonBeanRegistry singletonBeanRegistry = applicationContext.getBeanFactory();
    // 注册外部单例对象
    singletonBeanRegistry.registerSingleton("userSingletonBean", singletonUser);
    User userSingletonBean = applicationContext.getBean("userSingletonBean", User.class);
    System.out.println("user singleton bean: " + userSingletonBean);

    // Spring 内建可查找的BeanDefinition
    DefaultEventListenerFactory defaultEventListenerFactory =
        applicationContext.getBean(DefaultEventListenerFactory.class);
    System.out.println(defaultEventListenerFactory);
    Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap =
        applicationContext.getBeansOfType(BeanFactoryPostProcessor.class);
    beanFactoryPostProcessorMap.forEach(
        (k, v) -> System.out.printf("bean name: %s, bean class: %s\n", k, v));
    Map<String, BeanPostProcessor> beanPostProcessorMap =
        applicationContext.getBeansOfType(BeanPostProcessor.class);
    beanPostProcessorMap.forEach(
        (k, v) -> System.out.printf("bean name: %s, bean class: %s\n", k, v));

    String[] definitionNames = applicationContext.getBeanDefinitionNames();
    String[] beanNames =
        new String[] {
          "user",
          "superUser",
          "userBeanDefinition",
          "superUserBean",
          "dependencyLookupSourceDemo",
          "objectFactory"
        };
    Arrays.stream(definitionNames)
        .filter(beanName -> !Arrays.asList(beanNames).contains(beanName))
        .collect(Collectors.toList())
        .forEach(
            beanName ->
                System.out.printf(
                    "bean name: %s, bean class: %s\n",
                    beanName, applicationContext.getBean(beanName)));

    // Spring内建SingletonBean
    System.out.println("Spring内建SingletonBean ===== ");
    ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
    String[] singletonNames = beanFactory.getSingletonNames();
    Arrays.stream(singletonNames)
        .forEach(
            singletonName ->
                System.out.printf(
                    "bean name: %s, bean class: %s\n",
                    singletonName, beanFactory.getBean(singletonName)));

    applicationContext.close();
  }
}
