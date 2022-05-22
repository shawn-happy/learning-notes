package com.shawn.study.deep.in.spring.core.bean.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.shawn.study.deep.in.spring.core.bean.definition.config.BeanConfig;
import com.shawn.study.deep.in.spring.core.bean.definition.config.ComponentConfig;
import com.shawn.study.deep.in.spring.core.bean.definition.config.ImportConfig;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class BeanDefinitionRegisterTests {
  private static BeanDefinitionRegisterDemo register;

  @BeforeClass
  public static void init() {
    register = new BeanDefinitionRegisterDemo();
  }

  @Test
  public void testRegisterBeanFromXml() {
    ClassPathXmlApplicationContext applicationContext = register.registerBeanByXML();
    int beanDefinitionCount = applicationContext.getBeanDefinitionCount();
    assertEquals(3, beanDefinitionCount);
    String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    assertEquals(3, beanDefinitionNames.length);

    BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition("user");
    List<ValueHolder> genericArgumentValues =
        beanDefinition.getConstructorArgumentValues().getGenericArgumentValues();
    assertNotNull(genericArgumentValues);
    assertEquals(2, genericArgumentValues.size());

    Map<Integer, ValueHolder> indexedArgumentValues =
        beanDefinition.getConstructorArgumentValues().getIndexedArgumentValues();
    assertNotNull(indexedArgumentValues);
    assertEquals(2, indexedArgumentValues.size());
  }

  @Test
  public void testRegisterBeanFromAnnotationWithImportAndBean() {
    GenericApplicationContext applicationContext = register.registerBeanByAnnotationWithImport();
    int beanDefinitionCount = applicationContext.getBeanDefinitionCount();
    // 除了ImportConfig, BeanConfig, Person,还有5个内置的Spring Bean
    assertEquals(7, beanDefinitionCount);
    ImportConfig importConfig =
        applicationContext.getBean(ImportConfig.class.getTypeName(), ImportConfig.class);
    assertNotNull(importConfig);

    User person = applicationContext.getBean("importUser", User.class);
    assertNotNull(person);
    assertEquals("shawn", person.getName());

    BeanConfig bean = applicationContext.getBean("beanConfig", BeanConfig.class);
    assertNotNull(bean);
    bean.doSth();
  }

  @Test
  public void testRegisterBeanFromAnnotationWithComponent() {
    GenericApplicationContext genericApplicationContext =
        register.registerBeanByAnnotationWithComponent();
    BeanDefinition beanDefinition = genericApplicationContext.getBeanDefinition("componentConfig");
    if (beanDefinition instanceof AnnotatedGenericBeanDefinition) {
      int beanDefinitionCount = genericApplicationContext.getBeanDefinitionCount();
      System.out.println("beanDefinition Count: " + beanDefinitionCount);
      ComponentConfig componentConfig =
          genericApplicationContext.getBean("componentConfig", ComponentConfig.class);
      componentConfig.doSth();
    }
  }
}
