package com.shawn.study.deep.in.spring.core.ioc.bean.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class BeanNameAndAliasTests {

  private static BeanNameAndAliasDemo beanNameAndAlias;

  @BeforeClass
  public static void init() {
    beanNameAndAlias = new BeanNameAndAliasDemo();
  }

  @Test
  public void testReadFromBeanXML() {
    String[] beanNames = beanNameAndAlias.testBeanName();
    assertEquals(3, beanNames.length);
    assertEquals("user", beanNames[0]);
    assertEquals("com.shawn.study.deep.in.spring.core.ioc.domain.User#0", beanNames[1]);
    assertEquals("com.shawn.study.deep.in.spring.core.ioc.domain.User#1", beanNames[2]);
  }

  @Test
  public void testUniqueBeanName() {
    String beanNames = beanNameAndAlias.testUniqueBeanName();
    assertEquals("user#0", beanNames);
  }

  @Test
  public void testGenerateBeanName() {
    String beanName = beanNameAndAlias.testGenerateBeanName();
    assertEquals("com.shawn.study.deep.in.spring.core.ioc.domain.User#0", beanName);
  }

  @Test
  public void testCustomBeanNameGenerator() {
    String[] beanNames = beanNameAndAlias.testCustomBeanNameGenerator();
    assertEquals(1, beanNames.length);
    assertEquals("custom-user", beanNames[0]);
  }

  @Test
  public void testAliasName() {
    boolean b = beanNameAndAlias.testAlias();
    assertTrue(b);
  }

  @Test
  public void testAnnotationBeanNameGenerator() {
    String s = beanNameAndAlias.testAnnotationBeanNameGenerator();
    assertEquals("user", s);
  }

  @Test
  public void testDefaultBeanNameGenerator() {
    String s = beanNameAndAlias.testDefaultBeanNameGenerator();
    assertEquals("com.shawn.study.deep.in.spring.core.ioc.domain.User#0", s);
  }
}
