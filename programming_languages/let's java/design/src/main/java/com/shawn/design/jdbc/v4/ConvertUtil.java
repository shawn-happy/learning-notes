package com.shawn.design.jdbc.v4;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shawn
 * @since 2020/8/23
 */
public class ConvertUtil {
  public static <T> T convertDataType(Class<T> ptype, Object val) {
    T result = null;
    if (ptype == String.class) {
      if (StringUtil.isNotBlank(val)) {
        val = val.toString().trim();
      } else {
        val = "";
      }
    } else if (ptype == Boolean.class || ptype == boolean.class) {
      if (StringUtil.isNotBlank(val)) {
        if ("1".equals((String.valueOf(val))) || "true".equals((String.valueOf(val)))) {
          val = true;
        } else {
          val = false;
        }
      } else {
        if (ptype == boolean.class) {
          val = false;
        } else {
          val = null;
        }
      }
    } else if (ptype == Integer.class || ptype == int.class) {
      if (StringUtil.isNotBlank(val)) {
        String temp = String.valueOf(val);
        if (temp.toLowerCase().endsWith("px")) {
          temp = temp.substring(0, temp.indexOf("px"));
        } else if (temp.endsWith("%")) {
          val = null;
        }
        if (val != null) {
          val = Integer.parseInt(temp);
        }
      } else {
        if (ptype == int.class) {
          val = 0;
        } else {
          val = null;
        }
      }
    } else if (ptype == Date.class) {
      if (val != null && !(val instanceof Date)) {
        val = val.toString().replace("T", " ");
        val = convertString2DateAuto(val.toString());
      }
    } else if (ptype == Double.class || ptype == double.class) {
      if (StringUtil.isBlank(val)) {
        if (ptype == Double.class) {
          val = null;
        } else {
          val = Double.parseDouble("0");
        }
      } else {
        val = Double.parseDouble(String.valueOf(val));
      }
    } else if (ptype == Long.class || ptype == long.class) {
      if (StringUtil.isBlank(val)) {
        if (ptype == Long.class) {
          val = null;
        } else {
          val = 0L;
        }
      } else {
        val = Long.parseLong(String.valueOf(val));
      }
    } else if (ptype == Float.class || ptype == float.class) {
      if (StringUtil.isBlank(val)) {
        if (ptype == Float.class) {
          val = null;
        } else {
          val = Float.parseFloat("0");
        }
      } else {
        val = Float.parseFloat(String.valueOf(val));
      }
    } else if (ptype == Short.class || ptype == short.class) {
      if (StringUtil.isBlank(val)) {
        if (ptype == Short.class) {
          val = null;
        } else {
          val = Short.parseShort("0");
        }
      } else {
        val = Short.parseShort(String.valueOf(val));
      }
    } else if (ptype == BigDecimal.class) {
      if (StringUtil.isNotBlank(val)) {
        if (val instanceof Integer || val instanceof String) {
          val = new BigDecimal(String.valueOf(val));
        } else if (!(val instanceof BigDecimal)) {
          val = null;
        }
      }
    }
    if (val != null) {
      result = (T) val;
    }
    return result;
  }

  public static final String DATE_FORMAT = "yyyy-MM-dd";

  public static final String DATE_NO_SPLIT_FORMAT = "yyyyMMdd";

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static Date convertString2DateAuto(String dateStr) {
    Date date = null;
    if (StringUtil.isNotBlank(dateStr)) {
      dateStr = dateStr.trim();
      dateStr = dateStr.replace("T", " ");
      String format = DATE_NO_SPLIT_FORMAT;
      boolean haveTime = false;
      // 判断是否有时分秒
      if (dateStr.indexOf(":") != -1) {
        haveTime = true;
        // 如果没有秒、自动拼接上秒
        String[] a = dateStr.split(":");
        if (a.length == 2) {
          dateStr += ":00";
        }
      }
      if (dateStr.indexOf("-") != -1) {
        format = DATE_FORMAT;
      } else if (dateStr.indexOf("/") != -1) {
        format = "yyyy/MM/dd";
      } else {
        // 对年月日的写法做转换支持 edit by ko 2015-11-18
        if (dateStr.indexOf("年") != -1
            || dateStr.indexOf("月") != -1
            || dateStr.indexOf("日") != -1) {
          dateStr = dateStr.replaceAll("年", "-");
          dateStr = dateStr.replaceAll("月", "-");
          dateStr = dateStr.replaceAll("日", "");
          format = DATE_FORMAT;
        }
      }
      if (haveTime) {
        format += "HH:mm:ss";
      }
      SimpleDateFormat dateformat = new SimpleDateFormat(format);
      try {
        date = dateformat.parse(dateStr);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return date;
  }
}
