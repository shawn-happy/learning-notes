package com.shawn.study.deep.in.spring.aop.overview.interceptor;

import java.lang.reflect.Method;

public interface AfterReturnInterceptor {
  /**
   * 后置执行
   *
   * @param proxy
   * @param method
   * @param args
   * @param returnResult 执行方法返回结果
   * @return
   */
  Object after(Object proxy, Method method, Object[] args, Object returnResult);
}
