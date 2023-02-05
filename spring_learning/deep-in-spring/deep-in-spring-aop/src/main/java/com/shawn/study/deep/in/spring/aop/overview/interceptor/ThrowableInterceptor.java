package com.shawn.study.deep.in.spring.aop.overview.interceptor;

import java.lang.reflect.Method;

public interface ThrowableInterceptor {
  /**
   * @param proxy
   * @param method
   * @param args
   * @param throwable 异常信息
   */
  void intercept(Object proxy, Method method, Object[] args, Throwable throwable);
}
