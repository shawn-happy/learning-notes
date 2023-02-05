package com.shawn.study.deep.in.spring.aop.features.aspect;

import java.util.logging.Logger;

public class SimpleAspect {

  private final Logger logger = Logger.getLogger(SimpleAspect.class.getSimpleName());

  public void afterReturn(Object returnValue) throws Throwable {
    logger.info(String.format("result value is %s", returnValue));
  }
}
