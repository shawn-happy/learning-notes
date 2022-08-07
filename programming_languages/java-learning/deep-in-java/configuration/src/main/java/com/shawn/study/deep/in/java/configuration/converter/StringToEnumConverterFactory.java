package com.shawn.study.deep.in.java.configuration.converter;

import com.shawn.study.deep.in.java.configuration.utils.ConversionUtils;
import org.eclipse.microprofile.config.spi.Converter;

public class StringToEnumConverterFactory implements ConverterFactory<Enum<?>> {

  @Override
  public <T extends Enum<?>> Converter<T> getConverter(Class<T> targetType) {
    return new EnumConverter(ConversionUtils.getEnumType(targetType));
  }

  private static class EnumConverter<T extends Enum> extends GenericConverter<T> {

    public EnumConverter(Class<T> enumType) {
      super(enumType);
    }

    @Override
    protected T doConvert(String value) throws Throwable {
      return (T) Enum.valueOf(getType(), value);
    }
  }
}
