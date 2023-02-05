package com.shawn.study.deep.in.spring.aop.features.config;

import org.aspectj.lang.ProceedingJoinPoint;

public class AspectXmlConfiguration {

  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    System.out.println("@around any public method: " + pjp.getSignature());
    return pjp.proceed();
  }
}
