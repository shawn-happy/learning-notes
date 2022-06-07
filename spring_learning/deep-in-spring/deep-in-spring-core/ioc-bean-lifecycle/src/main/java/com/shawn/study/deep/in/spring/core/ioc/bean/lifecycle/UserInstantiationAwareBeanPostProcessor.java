package com.shawn.study.deep.in.spring.core.ioc.bean.lifecycle;

import com.shawn.study.deep.in.spring.core.ioc.domain.SuperUser;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.util.ObjectUtils;

public class UserInstantiationAwareBeanPostProcessor
    implements InstantiationAwareBeanPostProcessor {

  @Override
  public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName)
      throws BeansException {
    if ("superUser".equals(beanName) && SuperUser.class.equals(beanClass)) {
      SuperUser superUser = new SuperUser();
      superUser.setId("123456");
      superUser.setName("jack");
      superUser.setAddress("TianJing");
      superUser.setAge(27);
      superUser.setIdCard("123456789012");
      return superUser;
    }
    return null;
  }

  @Override
  public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
    if (ObjectUtils.nullSafeEquals("user", beanName) && User.class.equals(bean.getClass())) {
      User user = (User) bean;
      user.setId("654321");
      user.setName("john");
      return false;
    }
    return true;
  }

  @Override
  public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
      throws BeansException {
    // 对 "userHolder" Bean 进行拦截
    if (ObjectUtils.nullSafeEquals("userHolder", beanName)
        && UserHolder.class.equals(bean.getClass())) {
      // 假设 <property name="number" value="1" /> 配置的话，那么在 PropertyValues 就包含一个
      // PropertyValue(number=1)

      final MutablePropertyValues propertyValues;

      if (pvs instanceof MutablePropertyValues) {
        propertyValues = (MutablePropertyValues) pvs;
      } else {
        propertyValues = new MutablePropertyValues();
      }

      // 如果存在 "description" 属性配置的话
      if (propertyValues.contains("description")) {
        // PropertyValue value 是不可变的
        PropertyValue propertyValue = propertyValues.getPropertyValue("description");
        if (!ObjectUtils.isEmpty(propertyValue)) {
          System.out.println(propertyValue.getValue());
        }
        propertyValues.removePropertyValue("description");
        // Bean definition property value v1 -> postProcessProperties v2
        System.out.println("Bean definition property value v1 -> postProcessProperties v2");
        propertyValues.addPropertyValue("description", "The user holder V2");
      }

      return propertyValues;
    }
    return null;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    if (ObjectUtils.nullSafeEquals("userHolder", beanName)
        && UserHolder.class.equals(bean.getClass())) {
      UserHolder userHolder = (UserHolder) bean;
      // postProcessProperties v2 -> postProcessBeforeInitialization v3
      System.out.println("postProcessProperties v2 -> postProcessBeforeInitialization v3");
      userHolder.setDescription("The user holder v3");
      return userHolder;
    }
    return null;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (ObjectUtils.nullSafeEquals("userHolder", beanName)
        && UserHolder.class.equals(bean.getClass())) {
      UserHolder userHolder = (UserHolder) bean;
      // init method v6 -> postProcessAfterInitialization v7
      System.out.println("init method v6 -> postProcessAfterInitialization v7");
      userHolder.setDescription("The user holder v7");
      return userHolder;
    }
    return null;
  }
}
