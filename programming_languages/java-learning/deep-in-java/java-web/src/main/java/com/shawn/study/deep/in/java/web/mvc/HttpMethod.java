package com.shawn.study.deep.in.java.web.mvc;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public enum HttpMethod {
  GET,
  HEAD,
  POST,
  PUT,
  PATCH,
  DELETE,
  OPTIONS,
  TRACE;

  private static final Map<String, HttpMethod> mappings = new HashMap<>(16);

  static {
    for (HttpMethod httpMethod : HttpMethod.values()) {
      mappings.put(httpMethod.name(), httpMethod);
    }
  }

  public static HttpMethod of(String method) {
    return StringUtils.isBlank(method) ? null : mappings.get(method.toUpperCase());
  }

  public static boolean match(String method) {
    return of(method) != null;
  }
}
