package com.shawn.study.deep.in.java.configuration.utils;

public class StringUtils {

  public static String trimAllWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }

    int len = str.length();
    StringBuilder sb = new StringBuilder(str.length());
    for (int i = 0; i < len; i++) {
      char c = str.charAt(i);
      if (!Character.isWhitespace(c)) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static boolean hasLength(String str) {
    return (str != null && !str.isEmpty());
  }
}
