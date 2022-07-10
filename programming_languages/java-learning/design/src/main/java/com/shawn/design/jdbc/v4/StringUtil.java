package com.shawn.design.jdbc.v4;

/**
 * @author shawn
 * @since 2020/8/23
 */
public class StringUtil {

  public static boolean isBlank(final Object obj) {
    if (obj == null) {
      return true;
    }
    return isBlank(obj.toString());
  }

  public static boolean isBlank(final CharSequence cs) {
    final int strLen = length(cs);
    if (strLen == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static int length(final CharSequence cs) {
    return cs == null ? 0 : cs.length();
  }

  public static boolean isNotBlank(final CharSequence cs) {
    return !isBlank(cs);
  }

  public static boolean isNotBlank(final Object obj) {
    if (obj == null) {
      return false;
    }
    return isNotBlank(obj.toString());
  }
}
