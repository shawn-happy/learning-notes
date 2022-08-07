package com.shawn.study.deep.in.java.configuration.source;

import com.shawn.study.deep.in.java.baens.core.ComponentContext;
import com.shawn.study.deep.in.java.configuration.utils.ConfigSourceUtils;

public class JNDIConfigSource extends MapConfigSource {

  private static final String JNDI_CONFIG_SOURCE = "jndi.config";

  public JNDIConfigSource(ComponentContext context) {
    super(JNDI_CONFIG_SOURCE, ConfigSourceOrdinal.JNDI_CONFIG_ORDINAL.getOrdinal());
    prepareConfig(ConfigSourceUtils.toMap(context));
  }
}
