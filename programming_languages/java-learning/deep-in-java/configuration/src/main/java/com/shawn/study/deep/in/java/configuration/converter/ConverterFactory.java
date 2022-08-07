package com.shawn.study.deep.in.java.configuration.converter;

import org.eclipse.microprofile.config.spi.Converter;

public interface ConverterFactory<R> {

  <T extends R> Converter<T> getConverter(Class<T> targetType);
}
