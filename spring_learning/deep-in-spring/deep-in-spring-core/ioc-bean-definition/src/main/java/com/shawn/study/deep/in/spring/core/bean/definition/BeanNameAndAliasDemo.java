package com.shawn.study.deep.in.spring.core.bean.definition;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanNameAndAliasDemo {
  public String[] testBeanName() {
    ApplicationContext applicationContext =
        new ClassPathXmlApplicationContext("classpath:/bean-definition.xml");
    return applicationContext.getBeanNamesForType(User.class);
  }

  /** {@link BeanDefinitionReaderUtils#uniqueBeanName}示例 */
  public String testUniqueBeanName() {
    BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
    return BeanDefinitionReaderUtils.uniqueBeanName("user", registry);
  }

  /** {@link BeanDefinitionReaderUtils#generateBeanName}示例 */
  public String testGenerateBeanName() {
    BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
    BeanDefinition beanDefinition = createUserBeanDefinition();
    return BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry, false);
  }

  public String testAnnotationBeanNameGenerator() {
    BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
    BeanDefinition beanDefinition = createUserBeanDefinition();
    BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
    return beanNameGenerator.generateBeanName(beanDefinition, registry);
  }

  /**
   * {@link BeanNameAndAliasDemo#testGenerateBeanName()} {@link DefaultBeanNameGenerator}内部调用了
   * {@link BeanDefinitionReaderUtils#generateBeanName}
   *
   * @return
   */
  public String testDefaultBeanNameGenerator() {
    BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
    BeanDefinition beanDefinition = createUserBeanDefinition();
    BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
    return beanNameGenerator.generateBeanName(beanDefinition, registry);
  }

  /** {@link org.springframework.beans.factory.support.BeanNameGenerator} 示例 */
  public String[] testCustomBeanNameGenerator() {
    AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();
    applicationContext.setBeanNameGenerator(
        new AnnotationBeanNameGenerator() {
          @Override
          public String generateBeanName(
              BeanDefinition definition, BeanDefinitionRegistry registry) {
            return "custom-" + super.generateBeanName(definition, registry);
          }
        });
    applicationContext.register(BeanNameAndAliasDemo.class);
    applicationContext.refresh();
    applicationContext.registerBean(User.class);
    return applicationContext.getBeanNamesForType(User.class);
  }

  public boolean testAlias() {
    ApplicationContext applicationContext =
        new ClassPathXmlApplicationContext("classpath:/bean-definition.xml");
    User person = applicationContext.getBean("user", User.class);
    User aliasPerson = applicationContext.getBean("alias-user", User.class);
    return person == aliasPerson;
  }

  private BeanDefinition createUserBeanDefinition() {
    GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
    MutablePropertyValues pvs = new MutablePropertyValues();
    pvs.addPropertyValue(new PropertyValue("name", "shawn"));
    pvs.addPropertyValue(new PropertyValue("idCard", "123412312412"));
    pvs.addPropertyValue(new PropertyValue("age", 26));
    genericBeanDefinition.setPropertyValues(pvs);
    genericBeanDefinition.setBeanClass(User.class);
    return genericBeanDefinition;
  }
}
