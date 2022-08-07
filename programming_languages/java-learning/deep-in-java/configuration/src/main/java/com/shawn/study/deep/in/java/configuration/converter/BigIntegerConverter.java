package com.shawn.study.deep.in.java.configuration.converter;

import java.math.BigInteger;

public class BigIntegerConverter extends StringToNumberConverter<BigInteger> {

  public BigIntegerConverter() {
    super(BigInteger.class);
  }
}
