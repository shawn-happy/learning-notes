package com.shawn.study.deep.in.java.configuration.converter;

import com.shawn.study.deep.in.java.configuration.utils.NumberUtils;

class StringToNumberConverter<T extends Number> extends GenericConverter<T> {

  StringToNumberConverter(Class<T> classType) {
    super(classType);
  }

  @Override
  protected T doConvert(String value) throws Throwable {
    return NumberUtils.parseNumber(value, getType());
  }
}
