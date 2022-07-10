package com.shawn.study.deep.in.java.jndi;

import java.util.Hashtable;
import java.util.List;
import javax.naming.Context;
import org.junit.Test;

public class BeanContextTests {

  @Test
  public void test(){
    Hashtable<String, Object> env = new Hashtable<>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, DefaultBeanContextFactory.class);
    DefaultBeanContext beanContext = new DefaultBeanContext(env);

    beanContext.init();
    List<String> beanNames = beanContext.getBeanNames();
  }

}
