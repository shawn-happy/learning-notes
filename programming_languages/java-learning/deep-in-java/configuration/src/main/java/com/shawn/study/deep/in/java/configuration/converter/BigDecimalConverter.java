package com.shawn.study.deep.in.java.configuration.converter;

import java.math.BigDecimal;

public class BigDecimalConverter extends StringToNumberConverter<BigDecimal> {

  public BigDecimalConverter() {
    super(BigDecimal.class);
  }
}
