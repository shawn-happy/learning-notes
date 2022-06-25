package com.shawn.study.deep.in.spring.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan
public @interface MyComponentScan {
  @AliasFor(annotation = ComponentScan.class, attribute = "value") // 隐性别名
  // "多态"，子注解提供新的属性方法引用"父"（元）注解中的属性方法
  String[] scanBasePackages() default {"#"};
}
