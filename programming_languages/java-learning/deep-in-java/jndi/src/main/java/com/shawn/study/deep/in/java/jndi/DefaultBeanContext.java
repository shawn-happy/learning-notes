package com.shawn.study.deep.in.java.jndi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

public class DefaultBeanContext implements BeanContext {

  public static final String CONTEXT_NAME = DefaultBeanContext.class.getName();
  private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";
  private Context envContext;
  private ClassLoader classLoader;
  private Hashtable<?, ?> env;

  private ConcurrentMap<String, Object> beansMap = new ConcurrentHashMap<>();
  private List<String> beanNames = new ArrayList<>();

  public DefaultBeanContext() {}

  public DefaultBeanContext(Hashtable<?, ?> env) {
    this.env = env;
  }

  public void init() {
    this.classLoader = DefaultBeanContext.class.getClassLoader();
    this.envContext = getContext();
  }

  @Override
  public <C> C getBean(String name) {
    return null;
  }

  @Override
  public <C> C getBean(Class<?> requiredType) {
    return null;
  }

  @Override
  public List<String> getBeanNames() {
    try {
      beanNames.addAll(getBeanNames("/"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return beanNames;
  }

  public List<String> getBeanNames(String name) throws Exception {
    Context context = getContext();
    NamingEnumeration<NameClassPair> nameList = context.list(name);
    if (nameList == null) {
      return Collections.emptyList();
    }
    List<String> fullNames = new ArrayList<>();
    while (nameList.hasMoreElements()) {
      NameClassPair element = nameList.nextElement();
      String className = element.getClassName();
      Class<?> targetClass = classLoader.loadClass(className);
      if (Context.class.isAssignableFrom(targetClass)) {
        // 如果当前名称是目录（Context 实现类）的话，递归查找
        fullNames.addAll(getBeanNames(element.getName()));
      } else {
        // 否则，当前名称绑定目标类型的话话，添加该名称到集合中
        String fullName = name.startsWith("/") ? element.getName() : name + "/" + element.getName();
        fullNames.add(fullName);
      }
    }
    return fullNames;
  }

  public Context getContext() {
    if (this.envContext == null) {
      try {
        createContext();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return this.envContext;
  }

  public void createContext() throws Exception {
    Context context = null;
    try {
      context = new InitialContext(env);
      this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (context != null) {
        context.close();
      }
    }
  }
}
