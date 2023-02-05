package com.shawn.study.deep.in.spring.aop.service;

import org.springframework.util.StringUtils;

public class DefaultEchoService implements EchoService {

  @Override
  public String echo(String msg) throws NullPointerException {
    if (StringUtils.isEmpty(msg)) {
      throw new NullPointerException("msg is null");
    }
    return String.format("ECHO: [%s]", msg);
  }

  @Override
  public String display(String msg) {
    return String.format("display: [%s]", msg);
  }
}
