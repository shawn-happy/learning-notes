package com.shawn.study.deep.in.spring.core.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@MyComponentScan2(packages = "com.shawn.study.deep.in.spring.core.annotation")
public class AttributeOverridesDemo {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    // 注册 Configuration Class
    context.register(AttributeOverridesDemo.class);

    // 启动 Spring 应用上下文
    context.refresh();

    // 依赖查找 TestClass Bean
    // TestClass 标注 @MyComponent2
    // @MyComponent2 <- @MyComponent <- @Component
    // 从 Spring 4.0 开始支持多层次 @Component "派生"
    Test test = context.getBean(Test.class);

    // Annotation -> AnnotationAttributes(Map)

    System.out.println(test);

    // 关闭 Spring 应用上下文
    context.close();
  }
}
