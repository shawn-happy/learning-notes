package com.shawn.study.deep.in.spring.aop.features.pointcut;

import com.shawn.study.deep.in.spring.aop.service.EchoService;
import java.lang.reflect.Method;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

public class EchoServiceEchoMethodPointcut implements Pointcut {

  public static final EchoServiceEchoMethodPointcut INSTANCE = new EchoServiceEchoMethodPointcut();

  private EchoServiceEchoMethodPointcut(){}

  @Override
  public ClassFilter getClassFilter() {
    return EchoService.class::isAssignableFrom;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new MethodMatcher() {
      @Override
      public boolean matches(Method method, Class<?> targetClass) {
        return "echo".equals(method.getName())
            && method.getParameterCount() == 1
            && String.class.isAssignableFrom(method.getParameterTypes()[0]);
      }

      @Override
      public boolean isRuntime() {
        return false;
      }

      @Override
      public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return false;
      }
    };
  }
}
