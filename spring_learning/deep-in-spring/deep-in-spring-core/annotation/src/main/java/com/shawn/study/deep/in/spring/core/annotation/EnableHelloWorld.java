package com.shawn.study.deep.in.spring.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 第二步：通过 @Import 注解导入具体实现
// @Import(HelloWorldConfiguration.class) // 方法一： 通过 Configuration Class 实现
// @Import(HelloWorldImportSelector.class)// 方法二：通过 ImportSelector 接口实现
@Import(HelloWorldImportBeanDefinitionRegistrar.class) // 方法三：通过 ImportBeanDefinitionRegistrar
public @interface EnableHelloWorld {}
