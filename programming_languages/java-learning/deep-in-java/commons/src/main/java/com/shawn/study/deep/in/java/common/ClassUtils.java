package com.shawn.study.deep.in.java.common;

public class ClassUtils {
  public static ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Throwable ex) {
      // Cannot access thread context ClassLoader - falling back...
    }
    if (cl == null) {
      // No thread context class loader -> use class loader of this class.
      cl = ClassUtils.class.getClassLoader();
      if (cl == null) {
        // getClassLoader() returning null indicates the bootstrap ClassLoader
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (Throwable ex) {
          // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
        }
      }
    }
    return cl;
  }

  public static boolean instanceOf(Class<?> sourceClass, Class<?> targetClass) {
    boolean assignable = sourceClass.isAssignableFrom(targetClass);
    if (!assignable) {
      Class<?> superclass = targetClass.getSuperclass();
      if (superclass == null) {
        Class<?>[] interfaces = targetClass.getInterfaces();
        if (interfaces.length == 0) {
          return false;
        } else {
          for (Class<?> anInterface : interfaces) {
            return instanceOf(sourceClass, anInterface);
          }
        }
      } else {
        return instanceOf(sourceClass, superclass);
      }
    }
    return assignable;
  }

  public static boolean instanceOf(Class<?> sourceClass, String className) {
    Class<?> targetClass = null;
    try {
      targetClass = getDefaultClassLoader().loadClass(className);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return instanceOf(sourceClass, targetClass);
  }
}
