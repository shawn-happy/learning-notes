package com.shawn.study.deep.in.spring.core.ioc.bean.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.junit.BeforeClass;
import org.junit.Test;

public class BeanInstantiationTests {

  private static BeanInstantiationDemo instantiation;

  @BeforeClass
  public static void init() {
    instantiation = new BeanInstantiationDemo();
  }

  @Test
  public void testCreateBeanFromConstructor() {
    User user = instantiation.createUserByConstructor();
    assertNotNull(user);
    assertEquals("shawn", user.getId());
    assertEquals("shawn", user.getName());
  }

  @Test
  public void testCreateBeanFromFactoryMethod() {
    User user = instantiation.createUserByFactoryMethod();
    assertNotNull(user);
    assertEquals("1", user.getId());
    assertEquals("Shawn", user.getName());
  }

  @Test
  public void testCreateBeanFromBeanFactory() {
    User user = instantiation.createUserByInstanceFactory();
    assertNotNull(user);
    assertEquals("1", user.getId());
    assertEquals("Shawn", user.getName());
  }

  @Test
  public void testCreateBeanFromFactoryBean() {
    User user = instantiation.createUserByFactoryBean();
    assertNotNull(user);
    assertEquals("1", user.getId());
    assertEquals("Shawn", user.getName());
  }

  @Test
  public void testCreateBeanFromServiceLoader() {
    User user = instantiation.createUserByServiceLoader();
    assertNotNull(user);
    assertEquals("1", user.getId());
    assertEquals("Shawn", user.getName());
  }

  @Test
  public void testCreateBeanFromAutowireCapableBeanFactory() {
    User user = instantiation.createUserByAutowireCapableBeanFactory();
    assertNotNull(user);
    assertEquals("1", user.getId());
    assertEquals("Shawn", user.getName());
  }
}
