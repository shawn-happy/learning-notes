package com.shawn.study.java.configuration.converter;

public class LongConverter extends AbstractConverter<Long> {

  @Override
  protected Long doConvert(String value) {
    return Long.valueOf(value);
  }
}
