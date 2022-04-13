package com.shawn.study.deep.in.java.web.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServletResponseDemo {

  private static final Logger log = LogManager.getLogger(ServletResponseDemo.class);

  public static void buffer(ServletResponse response) {
    int bufferSize = response.getBufferSize();
    try (ServletOutputStream outputStream = response.getOutputStream()) {

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
