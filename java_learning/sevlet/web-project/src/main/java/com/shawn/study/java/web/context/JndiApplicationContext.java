package com.shawn.study.java.web.context;

import com.shawn.study.java.web.function.ThrowableAction;
import com.shawn.study.java.web.function.ThrowableFunction;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

/**
 * Web application context implemented by JNDI
 *
 * @author Shawn
 * @since 1.0.0
 */
public class JndiApplicationContext implements ApplicationContext {

  private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";
  private static final String CONTEXT_NAME = JndiApplicationContext.class.getName();
  private static final Logger LOGGER = Logger.getLogger(CONTEXT_NAME);
  private static final Consumer<Throwable> THROWABLE_CONSUMER =
      e -> LOGGER.log(Level.SEVERE, e.getMessage());

  private Context envContext;

  private ClassLoader classLoader;

  private static ServletContext servletContext;

  private Map<String, Object> componentCache = new ConcurrentHashMap<>();

  public void init(ServletContext servletContext) {
    JndiApplicationContext.servletContext = servletContext;
    this.classLoader = servletContext.getClassLoader();
    init();
    List<String> names = ThrowableFunction.execute("/", this::listComponentNames);
    names.forEach(name -> componentCache.put(name, createComponent(name)));
    componentCache
        .values()
        .forEach(component -> populateComponent(component, component.getClass()));

    servletContext.setAttribute(CONTEXT_NAME, this);
  }

  public static JndiApplicationContext getInstance() {
    if (servletContext != null) {
      return (JndiApplicationContext) servletContext.getAttribute(CONTEXT_NAME);
    }
    return null;
  }

  @Override
  public void init() {
    if (envContext != null) {
      return;
    }
    Context context = null;
    try {
      context = new InitialContext();
      envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
    } catch (NamingException e) {
      THROWABLE_CONSUMER.accept(e);
    } finally {
      close(context);
    }
  }

  @Override
  public <C> C createComponent(String name) {
    if (envContext != null) {
      ThrowableFunction<String, C> function = text -> (C) envContext.lookup(text);
      return function.execute(name);
    }
    return null;
  }

  @Override
  public <C> C getComponent(String name) {
    return (C) componentCache.get(name);
  }

  @Override
  public void destroy() {
    close(envContext);
  }

  private static void close(Context context) {
    if (context != null) {
      ThrowableAction.execute(context::close);
    }
  }

  private void populateComponent(Object component, Class<?> componentClass) {
    Stream.of(componentClass.getDeclaredFields())
        .filter(
            field ->
                !Modifier.isStatic(field.getModifiers())
                    && field.isAnnotationPresent(Resource.class))
        .forEach(
            field -> {
              Resource resource = field.getAnnotation(Resource.class);
              String name = resource.name();
              Object injectObject = null;
              if (componentCache != null && componentCache.containsKey(name)) {
                injectObject = componentCache.get(name);
              } else {
                injectObject = createComponent(name);
              }
              field.setAccessible(true);
              try {
                field.set(component, injectObject);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            });
  }

  private List<String> listComponentNames(String name) throws Exception {
    NamingEnumeration<NameClassPair> nameClassPairNamingEnumeration = envContext.list(name);
    if (nameClassPairNamingEnumeration == null) {
      return Collections.emptyList();
    }
    List<String> names = new ArrayList<>();
    while (nameClassPairNamingEnumeration.hasMoreElements()) {
      NameClassPair nameClassPair = nameClassPairNamingEnumeration.nextElement();
      String className = nameClassPair.getClassName();
      Class<?> clazz = classLoader.loadClass(className);
      if (Context.class.isAssignableFrom(clazz)) {
        names.addAll(listComponentNames(nameClassPair.getName()));
      } else {
        String fullName =
            name.startsWith("/") ? nameClassPair.getName() : name + "/" + nameClassPair.getName();
        names.add(fullName);
      }
    }
    return names;
  }
}
