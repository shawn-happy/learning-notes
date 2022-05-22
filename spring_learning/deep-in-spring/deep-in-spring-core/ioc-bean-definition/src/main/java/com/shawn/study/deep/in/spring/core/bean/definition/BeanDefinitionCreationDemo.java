package com.shawn.study.deep.in.spring.core.bean.definition;

import com.shawn.study.deep.in.spring.core.ioc.domain.SuperUser;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ChildBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * {@link org.springframework.beans.factory.config.BeanDefinition} 构建示例
 *
 * @author shawn
 * @since 2020/11/02
 */
public class BeanDefinitionCreationDemo {

  /** 使用{@link BeanDefinitionBuilder}创建 */
  public BeanDefinition createBeanDefinitionByBuilder() {
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
    builder
        .addPropertyValue("id", "shawn")
        .addPropertyValue("name", "shawn")
        .addPropertyValue("age", 26)
        .addPropertyValue("address", "shanghai");
    return builder.getBeanDefinition();
  }

  /**
   * 使用{@link AbstractBeanDefinition}的派生类创建
   *
   * @see RootBeanDefinition
   * @see ChildBeanDefinition
   * @see GenericBeanDefinition
   */
  public BeanDefinition createBeanDefinitionByAbstract() {
    AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClass(User.class);
    ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
    constructorArgumentValues.addIndexedArgumentValue(0, "shawn", String.class.getTypeName());
    ValueHolder valueHolder = new ValueHolder("shawn", String.class.getTypeName());
    constructorArgumentValues.addIndexedArgumentValue(1, valueHolder);
    constructorArgumentValues.addIndexedArgumentValue(2, 26, int.class.getTypeName());
    constructorArgumentValues.addIndexedArgumentValue(3, "shanghai", String.class.getTypeName());
    beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
    return beanDefinition;
  }

  /** {@link RootBeanDefinition} */
  public BeanDefinition createRootBeanDefinition() {
    MutablePropertyValues pvs = new MutablePropertyValues();
    pvs.addPropertyValue(new PropertyValue("id", "shawn"));
    pvs.addPropertyValue(new PropertyValue("name", "shawn"));
    pvs.addPropertyValue(new PropertyValue("address", "shanghai"));
    pvs.addPropertyValue(new PropertyValue("age", 26));
    RootBeanDefinition root = new RootBeanDefinition();
    root.setPropertyValues(pvs);
    root.setBeanClass(User.class);
    BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
    root.setBeanClassName(BeanDefinitionReaderUtils.generateBeanName(root, registry));
    return root;
  }

  /**
   * {@link ChildBeanDefinition}
   *
   * @return {@link ApplicationContext}
   */
  public ApplicationContext createChildBeanDefinition() {
    MutablePropertyValues pvs = new MutablePropertyValues();
    pvs.addPropertyValue(new PropertyValue("id", "shawn"));
    pvs.addPropertyValue(new PropertyValue("name", "shawn"));
    pvs.addPropertyValue(new PropertyValue("address", "shanghai"));
    pvs.addPropertyValue(new PropertyValue("age", 26));
    RootBeanDefinition root = new RootBeanDefinition("user");
    root.setBeanClass(User.class);
    root.setPropertyValues(pvs);

    ChildBeanDefinition childBeanDefinition = new ChildBeanDefinition("user");
    childBeanDefinition.setBeanClass(SuperUser.class);
    MutablePropertyValues pvsChild = new MutablePropertyValues();
    pvsChild.addPropertyValue(new PropertyValue("idCard", "1234567890"));
    childBeanDefinition.setPropertyValues(pvsChild);

    GenericApplicationContext registry = new AnnotationConfigApplicationContext();
    registry.registerBeanDefinition("user", root);
    registry.registerBeanDefinition("superUser", childBeanDefinition);
    registry.refresh();
    return registry;
  }

  /**
   * {@link GenericBeanDefinition}
   *
   * @return
   */
  public ApplicationContext createGenericBeanDefinition() {
    MutablePropertyValues pvs = new MutablePropertyValues();
    pvs.addPropertyValue(new PropertyValue("id", "shawn"));
    pvs.addPropertyValue(new PropertyValue("name", "shawn"));
    pvs.addPropertyValue(new PropertyValue("address", "shanghai"));
    pvs.addPropertyValue(new PropertyValue("age", 26));
    GenericBeanDefinition root = new GenericBeanDefinition();
    root.setBeanClass(User.class);
    root.setPropertyValues(pvs);

    GenericBeanDefinition childBeanDefinition = new GenericBeanDefinition();
    childBeanDefinition.setParentName("user");
    childBeanDefinition.setBeanClass(SuperUser.class);
    MutablePropertyValues pvsChild = new MutablePropertyValues();
    pvsChild.addPropertyValue(new PropertyValue("idCard", "1234567890"));
    pvsChild.addPropertyValue(new PropertyValue("address", "BEIJING"));
    childBeanDefinition.setPropertyValues(pvsChild);

    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.registerBeanDefinition("user", root);
    applicationContext.registerBeanDefinition("superUser", childBeanDefinition);
    applicationContext.refresh();
    return applicationContext;
  }
}
