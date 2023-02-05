package com.shawn.study.deep.in.spring.aop.features.proxyFactory;

import com.shawn.study.deep.in.spring.aop.features.interceptor.EchoServiceMethodInterceptor;
import com.shawn.study.deep.in.spring.aop.service.DefaultEchoService;
import com.shawn.study.deep.in.spring.aop.service.EchoService;
import org.springframework.aop.framework.ProxyFactory;

public class ProxyFactoryDemo {

  public static void main(String[] args) {
    EchoService echoService = new DefaultEchoService();
    ProxyFactory proxyFactory = new ProxyFactory(echoService);
    proxyFactory.addAdvice(new EchoServiceMethodInterceptor());
    EchoService proxy = (EchoService) proxyFactory.getProxy();
    System.out.println(proxy.echo("proxy factory demo"));
  }
}
