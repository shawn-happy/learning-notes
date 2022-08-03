package com.shawn.study.deep.in.java.configuration.source;

import com.shawn.study.deep.in.java.configuration.utils.ConfigSourceUtils;
import javax.servlet.ServletContext;

public class ServletContextConfigSource extends MapConfigSource {

  private static final String SERVLET_CONTEXT_CONFIG_NAME = "servlet.context.config";

  public ServletContextConfigSource(ServletContext servletContext) {
    super(
        SERVLET_CONTEXT_CONFIG_NAME,
        ConfigSourceUtils.toMap(servletContext),
        ConfigSourceOrdinal.SERVLET_CONTEXT_ORDINAL.getOrdinal());
  }
}
