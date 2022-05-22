package com.shawn.study.deep.in.spring.core.ioc.repository;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class UserRepository {
  private User[] users;

  private BeanFactory beanFactory;

  private ObjectFactory<User> userObjectFactory;

  private ObjectFactory<ApplicationContext> objectFactory;

  private Environment environment;

  public User[] getUsers() {
    return users;
  }

  public void setUsers(User[] users) {
    this.users = users;
  }

  public BeanFactory getBeanFactory() {
    return beanFactory;
  }

  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public ObjectFactory<User> getUserObjectFactory() {
    return userObjectFactory;
  }

  public void setUserObjectFactory(ObjectFactory<User> userObjectFactory) {
    this.userObjectFactory = userObjectFactory;
  }

  public ObjectFactory<ApplicationContext> getObjectFactory() {
    return objectFactory;
  }

  public void setObjectFactory(ObjectFactory<ApplicationContext> objectFactory) {
    this.objectFactory = objectFactory;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
