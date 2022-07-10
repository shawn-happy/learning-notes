package com.shawn.study.deep.in.java.web.servlet;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class ServletResponseDemo {

  private static final Logger log =
      LogManager.getLogManager().getLogger(ServletResponseDemo.class.getName());

  public static void buffer(ServletResponse response) {
    int bufferSize = response.getBufferSize();
    try (ServletOutputStream outputStream = response.getOutputStream()) {

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
