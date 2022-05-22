package com.shawn.study.deep.in.spring.core.bean.definition;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanDefinitionDemo {

  public static void main(String[] args) {
    ClassPathXmlApplicationContext applicationContext =
        new ClassPathXmlApplicationContext("classpath:/bean-definition.xml");
    User user = applicationContext.getBean("user", User.class);
    System.out.println(user);
    String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    int beanDefinitionCount = applicationContext.getBeanDefinitionCount();
    System.out.println("beanDefinitionCount = " + beanDefinitionCount);
    BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition("user");
    ConstructorArgumentValues constructorArgumentValues =
        beanDefinition.getConstructorArgumentValues();
    int argumentCount = constructorArgumentValues.getArgumentCount();
    System.out.println("user constructor Argument value count: " + argumentCount);
    Map<Integer, ValueHolder> indexedArgumentValues =
        constructorArgumentValues.getIndexedArgumentValues();
    List<ValueHolder> genericArgumentValues = constructorArgumentValues.getGenericArgumentValues();
    System.out.println("generic argument value count is: " + genericArgumentValues.size());
    System.out.printf(
        "param name is %s, the value is %s \n",
        genericArgumentValues.get(0).getName(), genericArgumentValues.get(0).getValue());
    indexedArgumentValues.forEach(
        (index, vh) ->
            System.out.printf(
                "the value of the %d param in the constructor params is：%s\n",
                index + 1, vh.getValue()));
  }
}
