package com.shawn.study.deep.in.java.configuration.utils;

import java.util.Objects;

public class ConversionUtils {

  public static Class<?> getEnumType(Class<?> targetType) {
    if (targetType == null) {
      throw new IllegalArgumentException("The target type can not be null");
    }
    Class<?> enumType = targetType;
    while (enumType != null && !enumType.isEnum()) {
      enumType = enumType.getSuperclass();
    }
    Objects.requireNonNull(
        enumType, () -> "The target type " + targetType.getName() + " does not refer to an enum");
    return enumType;
  }
}
