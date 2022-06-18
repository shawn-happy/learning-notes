package com.shawn.study.deep.in.spring.core.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class JavaMessageSourceDemo {

  private ResourceBundle resourceBundle;

  public void newUsResourceBundle(String baseName) {
    resourceBundle = ResourceBundle.getBundle(baseName, Locale.US);
  }

  public void newChinaResourceBundle(String baseName) {
    resourceBundle = ResourceBundle.getBundle(baseName, Locale.SIMPLIFIED_CHINESE);
  }

  public Locale getLocale() {
    return resourceBundle.getLocale();
  }

  public String getString(String key) {
    return resourceBundle.getString(key);
  }

  public String getString(String key, Object... params) {
    return MessageFormat.format(getString(key), params);
  }
}
