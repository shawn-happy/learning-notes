package com.shawn.study.deep.in.java.baens.core;

import com.shawn.study.deep.in.java.baens.exception.ComponentException;
import com.shawn.study.deep.in.java.common.ClassUtils;
import com.shawn.study.deep.in.java.common.ThrowableAction;
import com.shawn.study.deep.in.java.common.ThrowableFunction;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class DefaultComponentContext implements ComponentContext, ComponentContextLifCycle {

  private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

  private final List<String> componentNames = new ArrayList<>(256);
  private final Map<String, Object> componentMap = new ConcurrentHashMap<>(256);
  private final Map<Method, Object> destroyMethodMap = new ConcurrentHashMap<>(256);
  private Context envContext;

  public DefaultComponentContext() {
    init();
  }

  @Override
  public void init() {
    // 初始化JNDI容器
    initJNDIContext();
    // 实例化JNDI组件
    instantiateComponents();
    // 初始化JNDI组件
    initializeComponents();
    // 注册shutdown hook
    registerShutdownHook();
  }

  @Override
  public void destroy() {
    // 销毁组件
    processPreDestroy();
    // 清理缓存
    clearCache();
    // 关闭JNDI容器
    closeJNDIContext();
  }

  @Override
  public <C> C getComponent(String name) {
    Object component = componentMap.get(name);
    if (component == null) {
      throw new ComponentException("component not found by name: " + name);
    }
    return (C) component;
  }

  @Override
  public <C> C getComponent(String name, Class<C> type) {
    Object component = componentMap.get(name);
    if (component == null) {
      throw new ComponentException("component not found by name: " + name);
    }
    if (ClassUtils.instanceOf(type, component.getClass())) {
      return type.cast(component);
    }
    throw new ComponentException(
        String.format("component not found by name [%s] and type [%s]", name, type.getName()));
  }

  @Override
  public <C> C getComponent(Class<C> type) {
    Collection<Object> components = componentMap.values();
    for (Object component : components) {
      if (component.getClass().isAssignableFrom(type)) {
        return type.cast(component);
      }
    }
    throw new ComponentException("component not found by type: " + type.getName());
  }

  public List<String> getComponentNames() {
    return componentNames;
  }

  /** 初始化jndi容器阶段 */
  private void initJNDIContext() {
    if (this.envContext != null) {
      return;
    }
    Context context = null;
    try {
      context = new InitialContext();
      this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
    } catch (NamingException e) {
      throw new ComponentException("init jndi context error", e);
    } finally {
      close(context);
    }
  }

  /** 实例化组件阶段 */
  private void instantiateComponents() {
    this.componentNames.addAll(listAllComponentsNames());
    this.componentNames.forEach(name -> componentMap.put(name, lookupComponent(name)));
  }

  /** 初始化组件阶段 */
  private void initializeComponents() {
    componentMap.values().forEach(this::initializeComponent);
  }

  /** 注册shutdown hook */
  private void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(this::processPreDestroy));
  }

  private void processPreDestroy() {
    for (Method preDestroyMethod : destroyMethodMap.keySet()) {
      // 移除集合中的对象，防止重复执行 @PreDestroy 方法
      Object component = destroyMethodMap.remove(preDestroyMethod);
      // 执行目标方法
      ThrowableAction.execute(
          () -> preDestroyMethod.invoke(component),
          () -> new ComponentException("invoke component destroy method error"));
    }
  }

  private void clearCache() {
    componentMap.clear();
    destroyMethodMap.clear();
  }

  private void closeJNDIContext() {
    close(this.envContext);
  }

  private List<String> listAllComponentsNames() {
    return listComponentNames("/");
  }

  private List<String> listComponentNames(String path) {
    NamingEnumeration<NameClassPair> nameClassPairs =
        ThrowableFunction.execute(envContext, context -> context.list(path));
    if (nameClassPairs == null) {
      return Collections.emptyList();
    }
    List<String> fullNames = new LinkedList<>();
    while (nameClassPairs.hasMoreElements()) {
      NameClassPair nameClassPair = nameClassPairs.nextElement();
      String className = nameClassPair.getClassName();
      if (ClassUtils.instanceOf(Context.class, className)) {
        // 如果当前名称是目录(Context实现类)的话，递归查找
        fullNames.addAll(listComponentNames(nameClassPair.getName()));
      } else {
        String fullName =
            path.startsWith("/") ? nameClassPair.getName() : path + "/" + nameClassPair.getName();
        fullNames.add(fullName);
      }
    }
    return fullNames;
  }

  private <C> C lookupComponent(String name) {
    return ThrowableFunction.execute(
        envContext,
        context -> (C) context.lookup(name),
        () -> new ComponentException("look up component from jndi context error"));
  }

  private void initializeComponent(Object component) {
    Class<?> componentClass = component.getClass();
    // 依赖注入阶段 - {@link Resource}
    injectComponent(component, componentClass);
    List<Method> candidateMethods = findCandidateMethods(componentClass);
    // 初始阶段 - {@link PreConstruct}
    processPostConstruct(component, candidateMethods);
    // 销毁阶段 - {@link PreDestroy}
    processPreDestroyMetaData(component, candidateMethods);
  }

  private void injectComponent(Object component, Class<?> componentClass) {
    Field[] fields = componentClass.getDeclaredFields();
    Stream.of(fields)
        .filter(
            field ->
                !Modifier.isStatic(field.getModifiers())
                    && field.isAnnotationPresent(Resource.class))
        .forEach(
            field -> {
              Resource resource = field.getAnnotation(Resource.class);
              String name = resource.name();
              Object injectObject = lookupComponent(name);
              field.setAccessible(true);
              try {
                field.set(component, injectObject);
              } catch (IllegalAccessException ignore) {

              }
            });
  }

  private List<Method> findCandidateMethods(Class<?> componentClass) {
    return Stream.of(componentClass.getDeclaredMethods())
        .filter(
            method -> !Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0)
        .collect(Collectors.toList());
  }

  private void processPostConstruct(Object component, List<Method> methods) {
    methods
        .stream()
        .filter(method -> method.isAnnotationPresent(PostConstruct.class))
        .forEach(
            method ->
                ThrowableAction.execute(
                    () -> method.invoke(component),
                    () -> new ComponentException("invoke component init method error")));
  }

  private void processPreDestroyMetaData(Object component, List<Method> methods) {
    methods
        .stream()
        .filter(method -> method.isAnnotationPresent(PreDestroy.class))
        .forEach(method -> destroyMethodMap.put(method, component));
  }

  private static void close(Context context) {
    if (context != null) {
      ThrowableAction.execute(context::close);
    }
  }
}
