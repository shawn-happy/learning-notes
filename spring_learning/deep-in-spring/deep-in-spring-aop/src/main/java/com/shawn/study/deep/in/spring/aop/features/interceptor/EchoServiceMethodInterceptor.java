package com.shawn.study.deep.in.spring.aop.features.interceptor;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class EchoServiceMethodInterceptor implements MethodInterceptor {

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    System.out.printf("echoService拦截的方法是：%s\n", method.getName());
    return invocation.proceed();
  }
}
