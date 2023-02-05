package com.shawn.study.deep.in.spring.aop.features.pointcut;

import java.lang.reflect.Method;
import java.util.Objects;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

public class EchoServicePointCut extends StaticMethodMatcherPointcut {

  private final String methodName;
  private final Class<?> targetClass;

  public EchoServicePointCut(String methodName, Class<?> targetClass) {
    this.methodName = methodName;
    this.targetClass = targetClass;
  }

  @Override
  public boolean matches(Method method, Class<?> targetClass) {
    return Objects.equals(methodName, method.getName())
        && this.targetClass.isAssignableFrom(targetClass);
  }

  public String getMethodName() {
    return methodName;
  }

  public Class<?> getTargetClass() {
    return targetClass;
  }
}
