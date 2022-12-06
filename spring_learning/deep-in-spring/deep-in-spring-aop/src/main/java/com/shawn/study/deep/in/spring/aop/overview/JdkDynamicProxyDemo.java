package com.shawn.study.deep.in.spring.aop.overview;

import com.shawn.study.deep.in.spring.aop.service.DefaultEchoService;
import com.shawn.study.deep.in.spring.aop.service.EchoService;
import java.lang.reflect.Proxy;

public class JdkDynamicProxyDemo {

  public static void main(String[] args) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    EchoService service = new DefaultEchoService();
    Object obj =
        Proxy.newProxyInstance(
            classLoader,
            new Class[] {EchoService.class},
            (proxy, method, arg) -> {
              if (EchoService.class.isAssignableFrom(method.getDeclaringClass())) {
                System.out.println("before invoke echo...");
                Object result = method.invoke(service, arg);
                System.out.println(result);
                System.out.println("finish invoke echo...");
                return result;
              }
              return null;
            });
    EchoService echoService = (EchoService) obj;
    echoService.echo("Hello,World");
  }
}
