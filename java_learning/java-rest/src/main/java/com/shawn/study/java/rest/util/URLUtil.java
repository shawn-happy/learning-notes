package com.shawn.study.java.rest.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public interface URLUtil {

  static Map<String, List<String>> resolveParameters(String queryString) {
    Map<String, List<String>> paramMap = new HashMap<>();
    if (StringUtils.isNotBlank(queryString)) {

      String[] queryParams = StringUtils.split(queryString, "&");
      if (queryParams != null && queryParams.length > 0) {
        for (String queryParam : queryParams) {
          String[] paramAndValues = StringUtils.split(queryParam, "=");
          if (paramAndValues != null && paramAndValues.length > 0) {
            String paramName = paramAndValues[0];
            String value = paramAndValues.length > 1 ? paramAndValues[1] : StringUtils.EMPTY;
            List<String> values = paramMap.get(paramName);
            if (values == null || values.isEmpty()) {
              values = Collections.singletonList(value);
              paramMap.put(paramName, values);
            } else {
              values.add(value);
            }
          }
        }
      }
    }
    return Collections.unmodifiableMap(paramMap);
  }
}
