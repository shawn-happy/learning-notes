package com.shawn.study.java.configuration.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class StringConverter implements Converter<String> {

  @Override
  public String convert(String value) throws IllegalArgumentException, NullPointerException {
    return value;
  }
}
