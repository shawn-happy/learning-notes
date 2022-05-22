package com.shawn.study.deep.in.spring.core.ioc.dependency;

import com.shawn.study.deep.in.spring.core.ioc.annotation.Super;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.Map;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DependencyLookUpDemo {

  private final ApplicationContext applicationContext;

  public DependencyLookUpDemo() {
    applicationContext = new ClassPathXmlApplicationContext("classpath:/dependency-lookup.xml");
  }

  /** 根据BeanName：单一类型依赖查找 */
  public User lookupByBeanName() {
    return (User) applicationContext.getBean("user");
  }

  /** 根据BeanType：单一类型依赖查找 */
  public User lookupByBeanType() {
    return applicationContext.getBean(User.class);
  }

  /** 根据BeanName & BeanType：单一类型依赖查找 */
  public User lookupByBeanNameAndType() {
    return applicationContext.getBean("user", User.class);
  }

  /** 集合类型依赖查找 */
  public Map<String, User> lookupCollectionType() {
    return applicationContext.getBeansOfType(User.class);
  }

  /** 根据java annotation依赖查找 */
  public Map<String, Object> lookupBeansByAnnotation() {
    return applicationContext.getBeansWithAnnotation(Super.class);
  }

  /** 延迟依赖查找 */
  public User lookupInLazy() {
    ObjectFactory<User> objectFactory =
        (ObjectFactory<User>) applicationContext.getBean("objectFactory");
    return objectFactory.getObject();
  }
}
