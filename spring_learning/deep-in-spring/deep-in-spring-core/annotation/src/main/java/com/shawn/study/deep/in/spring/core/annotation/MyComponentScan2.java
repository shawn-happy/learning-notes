package com.shawn.study.deep.in.spring.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MyComponentScan
public @interface MyComponentScan2 {
  @AliasFor(annotation = MyComponentScan.class, attribute = "scanBasePackages") // 隐性别名
  String[] basePackages() default {};

  // @MyComponentScan2.basePackages
  // -> @MyComponentScan.scanBasePackages
  // -> @ComponentScan.value
  // -> @ComponentScan.basePackages

  /** 与元注解 @MyComponentScan 同名属性 */
  String[] scanBasePackages() default {};

  @AliasFor("scanBasePackages")
  String[] packages() default {}; // packages 覆盖了 scanBasePackages 覆盖了元注解 scanBasePackages
}
