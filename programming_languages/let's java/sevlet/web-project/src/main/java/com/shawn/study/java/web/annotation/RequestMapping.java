package com.shawn.study.java.web.annotation;

import com.shawn.study.java.web.HttpMethod;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * request mapping
 *
 * @author Shawn
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestMapping {
  String value() default "";

  HttpMethod method() default HttpMethod.GET;
}
