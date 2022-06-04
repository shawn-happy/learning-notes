package com.shawn.study.deep.in.spring.core.ioc.bean.definition;

import com.shawn.study.deep.in.spring.core.ioc.bean.definition.config.BeanConfig;
import com.shawn.study.deep.in.spring.core.ioc.bean.definition.config.ComponentConfig;
import com.shawn.study.deep.in.spring.core.ioc.bean.definition.factory.DefaultUserFactory;
import com.shawn.study.deep.in.spring.core.ioc.bean.definition.factory.UserFactory;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;

public class BeanDefinitionRegisterDemo {
  public ClassPathXmlApplicationContext registerBeanByXML() {
    return new ClassPathXmlApplicationContext("classpath:/bean-definition.xml");
  }

  /** {@link Import}示例 结果：有三个bean被注册： 1. BeanConfig 2. ImportConfig 3. Person */
  public GenericApplicationContext registerBeanByAnnotationWithImport() {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    // 实际上 AnnotationConfigApplicationContext.register 内部调用的 就是
    // AnnotatedBeanDefinitionReader#register方法
    applicationContext.register(BeanConfig.class);
    applicationContext.refresh();
    return applicationContext;
  }

  /** {@link org.springframework.stereotype.Component} 有两个bean被注册： 1. ComponentConfig 2. Person */
  public GenericApplicationContext registerBeanByAnnotationWithComponent() {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.register(ComponentConfig.class);
    applicationContext.refresh();
    return applicationContext;
  }

  /**
   * 命名方式 注册spring bean {@link BeanDefinitionRegistry#registerBeanDefinition(String,
   * BeanDefinition)}
   *
   * @param registry
   * @param beanName
   */
  private static void registerBeanDefinition(BeanDefinitionRegistry registry, String beanName) {
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
    AbstractBeanDefinition beanDefinition =
        builder
            .addPropertyValue("name", "shawn")
            .addPropertyValue("idCard", "123412312412")
            .addPropertyValue("age", 26)
            .getBeanDefinition();
    if (StringUtils.hasText(beanName)) {
      // 注册 BeanDefinition
      registry.registerBeanDefinition(beanName, beanDefinition);
    } else {
      // 非命名 Bean 注册方法
      BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
  }

  /**
   * 非命名方式 注册spring bean {@link
   * BeanDefinitionReaderUtils#registerWithGeneratedName(AbstractBeanDefinition,
   * BeanDefinitionRegistry)}
   *
   * @param registry
   */
  private static void registerBeanDefinitionWithoutBeanName(BeanDefinitionRegistry registry) {
    registerBeanDefinition(registry, null);
  }

  /** {@link SingletonBeanRegistry#registerSingleton(String, Object)}示例 */
  private static void registerSingletonBean() {
    // 创建 BeanFactory 容器
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    // 创建一个外部 UserFactory 对象
    UserFactory userFactory = new DefaultUserFactory();
    SingletonBeanRegistry singletonBeanRegistry = applicationContext.getBeanFactory();
    // 注册外部单例对象
    singletonBeanRegistry.registerSingleton("userFactory", userFactory);
    // 启动 Spring 应用上下文
    applicationContext.refresh();

    // 通过依赖查找的方式来获取 UserFactory
    UserFactory userFactoryByLookup = applicationContext.getBean("userFactory", UserFactory.class);
    System.out.println(
        "userFactory  == userFactoryByLookup : " + (userFactory == userFactoryByLookup));

    // 关闭 Spring 应用上下文
    applicationContext.close();
  }
}
