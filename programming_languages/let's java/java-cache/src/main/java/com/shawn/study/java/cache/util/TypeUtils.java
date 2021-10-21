package com.shawn.study.java.cache.util;

import static java.util.Collections.emptyList;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;

public class TypeUtils {

  private TypeUtils() {}

  public static List<Class<?>> resolveTypeArguments(Class<?> targetClass) {
    List<Class<?>> typeArguments = emptyList();
    while (targetClass != null) {
      typeArguments = resolveTypeArgumentsFromInterfaces(targetClass);
      if (!typeArguments.isEmpty()) {
        break;
      }

      Type superType = targetClass.getGenericSuperclass();
      if (superType instanceof ParameterizedType) {
        typeArguments = resolveTypeArgumentsFromType(superType);
      }

      if (!typeArguments.isEmpty()) {
        break;
      }
      // recursively
      targetClass = targetClass.getSuperclass();
    }

    return typeArguments;
  }

  private static List<Class<?>> resolveTypeArgumentsFromInterfaces(Class<?> type) {
    List<Class<?>> typeArguments = emptyList();
    for (Type superInterface : type.getGenericInterfaces()) {
      typeArguments = resolveTypeArgumentsFromType(superInterface);
      if (typeArguments != null && !typeArguments.isEmpty()) {
        break;
      }
    }
    return typeArguments;
  }

  private static List<Class<?>> resolveTypeArgumentsFromType(Type type) {
    List<Class<?>> typeArguments = emptyList();
    if (type instanceof ParameterizedType) {
      typeArguments = new LinkedList<>();
      ParameterizedType pType = (ParameterizedType) type;
      if (pType.getRawType() instanceof Class) {
        for (Type argument : pType.getActualTypeArguments()) {
          Class<?> typeArgument = asClass(argument);
          if (typeArgument != null) {
            typeArguments.add(typeArgument);
          }
        }
      }
    }
    return typeArguments;
  }

  private static Class<?> asClass(Type typeArgument) {
    if (typeArgument instanceof Class) {
      return (Class<?>) typeArgument;
    } else if (typeArgument instanceof TypeVariable) {
      TypeVariable typeVariable = (TypeVariable) typeArgument;
      return asClass(typeVariable.getBounds()[0]);
    }
    return null;
  }
}
