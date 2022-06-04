package com.shawn.study.deep.in.spring.core.ioc.bean.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.shawn.study.deep.in.spring.core.ioc.domain.SuperUser;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.context.ApplicationContext;

public class BeanDefinitionCreationTests {

  private static BeanDefinitionCreationDemo creation;

  @BeforeClass
  public static void init() {
    creation = new BeanDefinitionCreationDemo();
  }

  @Test
  public void testCreateBeanDefinitionByBuilder() {
    BeanDefinition beanDefinition = creation.createBeanDefinitionByBuilder();
    MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
    assertEquals(4, propertyValues.size());
    List<PropertyValue> propertyValueList = propertyValues.getPropertyValueList();
    propertyValueList.forEach(
        pv -> System.out.printf("{\"%s\": \"%s\"}, ", pv.getName(), pv.getValue()));
  }

  @Test
  public void testCreateBeanDefinitionByAbstract() {
    BeanDefinition beanDefinition = creation.createBeanDefinitionByAbstract();
    ConstructorArgumentValues constructorArgumentValues =
        beanDefinition.getConstructorArgumentValues();
    assertEquals(4, constructorArgumentValues.getArgumentCount());
    ValueHolder idVH = constructorArgumentValues.getIndexedArgumentValue(0, String.class);
    assertNotNull(idVH);
    assertEquals("shawn", idVH.getValue());

    ValueHolder nameVH = constructorArgumentValues.getIndexedArgumentValue(1, String.class);
    assertNotNull(nameVH);
    assertEquals("shawn", nameVH.getValue());

    ValueHolder ageVH = constructorArgumentValues.getIndexedArgumentValue(2, int.class);
    assertNotNull(ageVH);
    assertEquals(26, ageVH.getValue());

    ValueHolder addressVH = constructorArgumentValues.getIndexedArgumentValue(3, String.class);
    assertNotNull(addressVH);
    assertEquals("shanghai", addressVH.getValue());
  }

  @Test
  public void testCreateRootBeanDefinition() {
    BeanDefinition beanDefinition = creation.createRootBeanDefinition();
    String beanClassName = beanDefinition.getBeanClassName();
    assertEquals("com.shawn.study.deep.in.spring.core.ioc.domain.User#0", beanClassName);
    MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
    assertEquals(4, propertyValues.size());
    List<PropertyValue> propertyValueList = propertyValues.getPropertyValueList();
    propertyValueList.forEach(
        pv -> System.out.printf("{\"%s\": \"%s\"}, ", pv.getName(), pv.getValue()));
  }

  @Test
  public void testCreateChildBeanDefinition() {
    ApplicationContext applicationContext = creation.createChildBeanDefinition();
    SuperUser superUser = applicationContext.getBean("superUser", SuperUser.class);
    assertNotNull(superUser);
    assertEquals("shawn", superUser.getName());
    assertEquals("shawn", superUser.getId());
    assertEquals("1234567890", superUser.getIdCard());
    assertEquals(26, superUser.getAge());
    assertEquals("shanghai", superUser.getAddress());
  }

  @Test
  public void testCreateGenericBeanDefinition() {
    ApplicationContext applicationContext = creation.createGenericBeanDefinition();
    SuperUser superUser = applicationContext.getBean("superUser", SuperUser.class);
    assertNotNull(superUser);
    assertEquals("shawn", superUser.getName());
    assertEquals("shawn", superUser.getId());
    assertEquals("1234567890", superUser.getIdCard());
    assertEquals(26, superUser.getAge());
    assertEquals("BEIJING", superUser.getAddress());
  }
}
