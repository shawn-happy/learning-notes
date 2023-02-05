package com.shawn.study.deep.in.spring.aop.overview;

import com.shawn.study.deep.in.spring.aop.overview.interceptor.AfterReturnInterceptor;
import com.shawn.study.deep.in.spring.aop.overview.interceptor.BeforeInterceptor;
import com.shawn.study.deep.in.spring.aop.overview.interceptor.ThrowableInterceptor;
import com.shawn.study.deep.in.spring.aop.service.DefaultEchoService;
import com.shawn.study.deep.in.spring.aop.service.EchoService;
import java.lang.reflect.Proxy;

public class AopOverview {

  public static void main(String[] args) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    EchoService service = new DefaultEchoService();
    Object obj =
        Proxy.newProxyInstance(
            classLoader,
            new Class[] {EchoService.class},
            (proxy, method, arg) -> {
              if (EchoService.class.isAssignableFrom(method.getDeclaringClass())) {
                BeforeInterceptor beforeInterceptor = (p, m1, ps) -> System.currentTimeMillis();
                Long startTime = 0L;
                Long endTime = 0L;
                Object result = null;
                try {
                  startTime = (Long) beforeInterceptor.before(proxy, method, arg);
                  result = method.invoke(service, arg);
                  System.out.println(result);
                  System.out.println("finish invoke echo...");
                } catch (Throwable e) {
                  ThrowableInterceptor throwableInterceptor = (p, m1, ps, t) -> t.printStackTrace();
                  throwableInterceptor.intercept(proxy, method, arg, e);
                } finally {
                  AfterReturnInterceptor afterReturnInterceptor =
                      (p, m1, ps, res) -> System.currentTimeMillis();
                  endTime = (Long) afterReturnInterceptor.after(proxy, method, arg, result);
                  System.out.println(endTime - startTime);
                }
                return result;
              }
              return null;
            });
    EchoService echoService = (EchoService) obj;
    echoService.echo("Hello,World");
  }
}
