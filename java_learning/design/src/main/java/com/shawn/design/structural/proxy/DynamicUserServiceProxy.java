package com.shawn.design.structural.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author shawn
 * @description 动态代理
 *     <li>1.实现InvocationHandler接口
 *     <li>2.Proxy.newProxyInstance(ClassLoader, Class, InvocationHandler);
 * @since 2020/7/19
 */
public class DynamicUserServiceProxy {

  private UserService userService;

  public DynamicUserServiceProxy() {
    userService = new UserServiceImpl();
  }

  public Object createProxy(Object proxyObject) {
    Class<?>[] interfaces = proxyObject.getClass().getInterfaces();
    DynamicProxyHandler handler = new DynamicProxyHandler(proxyObject);
    return Proxy.newProxyInstance(proxyObject.getClass().getClassLoader(), interfaces, handler);
  }

  private class DynamicProxyHandler implements InvocationHandler {

    private Object proxyObject;

    public DynamicProxyHandler(Object proxyObject) {
      this.proxyObject = proxyObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      long start = System.currentTimeMillis();
      Object result = method.invoke(proxyObject, args);
      long end = System.currentTimeMillis();
      System.out.printf("id: %s, time: %sms\n", Arrays.toString(args), (end - start));
      return result;
    }
  }
}
