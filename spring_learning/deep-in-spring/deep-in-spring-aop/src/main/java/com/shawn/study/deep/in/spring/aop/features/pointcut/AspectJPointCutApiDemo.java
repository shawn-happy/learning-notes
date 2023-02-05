package com.shawn.study.deep.in.spring.aop.features.pointcut;

import com.shawn.study.deep.in.spring.aop.features.interceptor.EchoServiceMethodInterceptor;
import com.shawn.study.deep.in.spring.aop.service.DefaultEchoService;
import com.shawn.study.deep.in.spring.aop.service.EchoService;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class AspectJPointCutApiDemo {

  public static void main(String[] args) {
    EchoServicePointCut echoServicePointCut = new EchoServicePointCut("echo", EchoService.class);
    EchoServiceEchoMethodPointcut echoServiceEchoMethodPointcut =
        EchoServiceEchoMethodPointcut.INSTANCE;
    ComposablePointcut pointCut = new ComposablePointcut(echoServiceEchoMethodPointcut);
    pointCut.intersection(echoServicePointCut.getClassFilter());
    pointCut.intersection(echoServicePointCut.getMethodMatcher());
    // 将Pointcut适配称Advisor
    DefaultPointcutAdvisor advisor =
        new DefaultPointcutAdvisor(pointCut, new EchoServiceMethodInterceptor());
    ProxyFactory proxyFactory = new ProxyFactory(new DefaultEchoService());
    proxyFactory.addAdvisor(advisor);
    EchoService proxy = (EchoService) proxyFactory.getProxy();
    System.out.println(proxy.echo("aspectJ pointcut api demo"));
  }
}
