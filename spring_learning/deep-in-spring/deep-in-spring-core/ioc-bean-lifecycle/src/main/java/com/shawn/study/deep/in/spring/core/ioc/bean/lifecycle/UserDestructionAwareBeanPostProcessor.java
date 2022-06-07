package com.shawn.study.deep.in.spring.core.ioc.bean.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.util.ObjectUtils;

public class UserDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {

  @Override
  public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
    if (ObjectUtils.nullSafeEquals("userHolder", beanName)
        && UserHolder.class.equals(bean.getClass())) {
      UserHolder userHolder = (UserHolder) bean;
      // afterSingletonsInstantiated v8 -> postProcessBeforeDestruction v9
      System.out.println("afterSingletonsInstantiated v8 -> postProcessBeforeDestruction v9");
      userHolder.setDescription("The user holder v9");
    }
  }
}
