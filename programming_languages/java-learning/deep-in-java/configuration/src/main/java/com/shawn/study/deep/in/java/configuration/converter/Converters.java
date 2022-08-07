package com.shawn.study.deep.in.java.configuration.converter;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.eclipse.microprofile.config.spi.Converter;

public interface Converters extends Iterable<Converter<?>> {

  default Stream<Converter<?>> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  List<Converter<?>> getConverters(Class<?> convertedType);

  List<Converter<?>> getConverters();

  Converter<?> getConverter(Class<?> type);
}
