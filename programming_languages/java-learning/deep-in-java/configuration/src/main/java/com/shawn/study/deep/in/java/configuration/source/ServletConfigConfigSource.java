package com.shawn.study.deep.in.java.configuration.source;

import static com.shawn.study.deep.in.java.configuration.utils.ConfigSourceUtils.toMap;

import javax.servlet.ServletConfig;

public class ServletConfigConfigSource extends MapConfigSource {

  private static final String SERVLET_CONFIG_NAME = "servlet.config";

  public ServletConfigConfigSource(ServletConfig servletConfig) {
    super(SERVLET_CONFIG_NAME, ConfigSourceOrdinal.SERVLET_CONFIG_ORDINAL.getOrdinal());
    prepareConfig(toMap(servletConfig));
  }
}
