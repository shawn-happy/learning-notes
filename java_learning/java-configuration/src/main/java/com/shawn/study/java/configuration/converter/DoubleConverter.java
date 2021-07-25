package com.shawn.study.java.configuration.converter;

public class DoubleConverter extends AbstractConverter<Double> {

  @Override
  protected Double doConvert(String value) {
    return Double.valueOf(value);
  }
}
