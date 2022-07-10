package com.shawn.study.deep.in.java;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingDemo {

  private static final Logger LOGGER = LogManager.getLogger(LoggingDemo.class);

  public static void main(String[] args) {
    LOGGER.info("Hello World");
    System.out.println("Hello World");
  }
}
