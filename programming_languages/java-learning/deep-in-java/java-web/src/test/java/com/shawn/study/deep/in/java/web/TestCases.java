package com.shawn.study.deep.in.java.web;

import com.shawn.study.deep.in.java.web.controller.TestController;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class TestCases {

  @Test
  public void testComponentScan() {
    DispatcherServlet servlet = new DispatcherServlet();
    servlet.doScanner(null);
    Map<String, Object> controllerMaps = servlet.getControllerMaps();
    Assert.assertNotNull(controllerMaps);
    Object controller = controllerMaps.values().iterator().next();
    Assert.assertEquals(TestController.class, controller.getClass());
  }
}
