package com.shawn.study.java.configuration.converter;

public class FloatConverter extends AbstractConverter<Float> {

  @Override
  protected Float doConvert(String value) {
    return Float.valueOf(value);
  }
}
