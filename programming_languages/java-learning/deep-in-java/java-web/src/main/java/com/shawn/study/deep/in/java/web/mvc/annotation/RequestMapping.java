package com.shawn.study.deep.in.java.web.mvc.annotation;

import com.shawn.study.deep.in.java.web.mvc.HttpMethod;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

  String value() default "";

  HttpMethod[] method() default {
    HttpMethod.GET,
    HttpMethod.POST,
    HttpMethod.DELETE,
    HttpMethod.PUT,
    HttpMethod.HEAD,
    HttpMethod.PATCH,
    HttpMethod.OPTIONS
  };
}
