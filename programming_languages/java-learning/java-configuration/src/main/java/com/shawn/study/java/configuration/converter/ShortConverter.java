package com.shawn.study.java.configuration.converter;

public class ShortConverter extends AbstractConverter<Short> {

  @Override
  protected Short doConvert(String value) {
    return Short.valueOf(value);
  }
}
