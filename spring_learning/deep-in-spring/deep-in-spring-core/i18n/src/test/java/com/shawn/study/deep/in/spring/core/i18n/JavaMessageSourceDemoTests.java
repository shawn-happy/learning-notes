package com.shawn.study.deep.in.spring.core.i18n;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.junit.Test;

public class JavaMessageSourceDemoTests {

  @Test
  public void testNewUsResourceBundle() {
    JavaMessageSourceDemo demo = new JavaMessageSourceDemo();
    demo.newUsResourceBundle("messages");
    Locale locale = demo.getLocale();
    assertEquals(Locale.US, locale);
    String message = demo.getString("message");
    assertEquals("Hello world", message);
    String errorMsg = demo.getString("error.message", "Integer");
    assertEquals("data type [Integer] not found", errorMsg);
  }

  @Test
  public void testNewZhCNResourceBundle() {
    JavaMessageSourceDemo demo = new JavaMessageSourceDemo();
    demo.newChinaResourceBundle("messages");
    Locale locale = demo.getLocale();
    assertEquals(Locale.SIMPLIFIED_CHINESE, locale);
    String message = demo.getString("message");
    assertEquals("你好，世界", message);
    String errorMsg = demo.getString("error.message", "Integer");
    assertEquals("数据类型[Integer]未找到", errorMsg);
  }
}
