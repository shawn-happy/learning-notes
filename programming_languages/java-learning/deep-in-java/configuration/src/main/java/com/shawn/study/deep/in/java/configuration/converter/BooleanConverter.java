package com.shawn.study.deep.in.java.configuration.converter;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class BooleanConverter extends GenericConverter<Boolean> {

  private static final Set<String> trueValues = new HashSet<>(4);

  private static final Set<String> falseValues = new HashSet<>(4);

  static {
    trueValues.add("true");
    trueValues.add("on");
    trueValues.add("yes");
    trueValues.add("1");

    falseValues.add("false");
    falseValues.add("off");
    falseValues.add("no");
    falseValues.add("0");
  }

  public BooleanConverter() {
    super(Boolean.class);
  }

  @Override
  protected Boolean doConvert(String text) throws Throwable {
    String value = StringUtils.trim(text);
    if (value.isEmpty()) {
      return null;
    }
    value = value.toLowerCase();
    if (trueValues.contains(value)) {
      return Boolean.TRUE;
    } else if (falseValues.contains(value)) {
      return Boolean.FALSE;
    } else {
      throw new IllegalArgumentException("Invalid boolean value '" + text + "'");
    }
  }
}
