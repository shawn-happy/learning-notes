package com.shawn.study.deep.in.spring.aop.overview;

import com.shawn.study.deep.in.spring.aop.service.DefaultEchoService;
import com.shawn.study.deep.in.spring.aop.service.EchoService;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class CglibDynamicProxyDemo {

  public static void main(String[] args) {
    Enhancer enhancer = new Enhancer();
    Class<?> superClass = DefaultEchoService.class;
    enhancer.setSuperclass(superClass);
    enhancer.setInterfaces(new Class[] {EchoService.class});
    enhancer.setCallback(
        (MethodInterceptor)
            (source, method, objects, methodProxy) -> {
              long startTime = System.currentTimeMillis();
              // Source -> CGLIB 子类
              // 目标类  -> DefaultEchoService
              // 错误使用
              // Object result = method.invoke(source, args);
              // 正确的方法调用
              Object result = methodProxy.invokeSuper(source, objects);
              long costTime = System.currentTimeMillis() - startTime;
              System.out.println("[CGLIB 字节码提升] echo 方法执行的实现：" + costTime + " ms.");
              return result;
            });
    // 创建代理对象
    EchoService echoService = (EchoService) enhancer.create();
    // 输出执行结果
    System.out.println(echoService.echo("Hello,World"));
  }
}
