package com.shawn.study.deep.in.java.configuration.converter;

import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.spi.Converter;

public abstract class GenericConverter<T> implements Converter<T> {

  private final Class<T> type;

  public GenericConverter(Class<T> type) {
    this.type = type;
  }

  @Override
  public T convert(String value) throws IllegalArgumentException, NullPointerException {
    if (StringUtils.isBlank(value)) {
      throw new NullPointerException("The value must not be null!");
    }
    T convertedValue = null;
    try {
      convertedValue = doConvert(value);
    } catch (Throwable e) {
      throw new IllegalArgumentException("The value can't be converted.", e);
    }
    return convertedValue;
  }

  protected abstract T doConvert(String value) throws Throwable;

  public Class<T> getType() {
    return type;
  }
}
