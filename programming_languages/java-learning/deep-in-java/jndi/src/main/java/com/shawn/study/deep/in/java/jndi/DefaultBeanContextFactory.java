package com.shawn.study.deep.in.java.jndi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

public class DefaultBeanContextFactory implements ObjectFactory {

  @Override
  public Object getObjectInstance(
      Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
    if (obj == null) {
      return new DefaultBeanContext(environment);
    }
    return null;
  }
}
