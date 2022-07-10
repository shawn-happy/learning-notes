package com.shawn.study.deep.in.java.web;

import static java.util.Arrays.asList;

import com.shawn.study.deep.in.java.web.mvc.HttpMethod;
import com.shawn.study.deep.in.java.web.mvc.annotation.Controller;
import com.shawn.study.deep.in.java.web.mvc.annotation.RequestBody;
import com.shawn.study.deep.in.java.web.mvc.annotation.RequestMapping;
import com.shawn.study.deep.in.java.web.mvc.annotation.ResponseBody;
import com.shawn.study.deep.in.java.web.mvc.handle.HandleMethodInfo;
import com.shawn.study.deep.in.java.web.mvc.view.JspViewResolver;
import com.shawn.study.deep.in.java.web.mvc.view.ViewResolver;
import com.shawn.study.deep.in.java.web.utils.JsonUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.StringUtils;

public class DispatcherServlet extends HttpServlet {

  private final ViewResolver viewResolver = new JspViewResolver("/", ".jsp");
  private final List<String> beanNames = new ArrayList<>(256);
  private final Map<String, Object> controllerMaps = new ConcurrentHashMap<>(256);
  private final Map<String, HandleMethodInfo> handleMethodInfoMap = new ConcurrentHashMap<>(256);

  @Override
  public void init() throws ServletException {
    String componentScanPackage = getServletConfig().getInitParameter("componentScanPackage");
    doScanner(componentScanPackage);
    initHandlerMethods();
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doDispatcher(request, response);
  }

  public void doDispatcher(HttpServletRequest request, HttpServletResponse response)
      throws ServletException {
    String requestURI = request.getRequestURI();
    String prefixPath = request.getContextPath();
    String requestMappingPath =
        StringUtils.substringAfter(requestURI, StringUtils.replace(prefixPath, "//", "/"));
    HandleMethodInfo handleMethodInfo = handleMethodInfoMap.get(requestMappingPath);
    if (handleMethodInfo == null || handleMethodInfo.getControllerClass() == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    Object controller = handleMethodInfo.getControllerClass();
    Method method = handleMethodInfo.getMethod();
    Set<HttpMethod> httpMethods = handleMethodInfo.getHttpMethods();
    if (!httpMethods.contains(HttpMethod.of(request.getMethod()))) {
      response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      return;
    }
    Map<String, String[]> parameterMap = request.getParameterMap();
    Map<String, String> methodParameter = new HashMap<>();
    for (Entry<String, String[]> entry : parameterMap.entrySet()) {
      String parameterName = entry.getKey();
      String value =
          Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
      methodParameter.put(parameterName, value);
    }
    Parameter[] parameters = method.getParameters();
    Object[] params = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      String requestParam = parameter.getType().getSimpleName();
      if (requestParam.equals("HttpServletRequest")) {
        params[i] = parameter;
        continue;
      }
      if (requestParam.equals("HttpServletResponse")) {
        params[i] = parameter;
        continue;
      }
      if (parameter.isAnnotationPresent(RequestBody.class)) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
            BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(inputStream)); ) {
          char[] charBuffer = new char[128];
          int bytesRead;
          while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
            stringBuilder.append(charBuffer, 0, bytesRead);
          }
          Class<?> type = parameter.getType();
          params[i] = JsonUtil.readValue(stringBuilder.toString(), type);
        } catch (IOException e) {
          throw new ServletException(e);
        }
      } else if (parameter.getAnnotations().length == 0) {
        params[i] = methodParameter.get(parameter.getName());
      }
    }

    // 利用反射机制来调用
    try (PrintWriter writer = response.getWriter()) {
      Object result = method.invoke(controller, params);
      if (Objects.isNull(result)) {
        log("no result");
      } else {
        // 如果标记为forward，则执行forward请求
        if (!method.isAnnotationPresent(ResponseBody.class)) {
          request
              .getRequestDispatcher(viewResolver.resolve(result.toString()))
              .forward(request, response);
        } else {
          writer.write(
              JsonUtil.toJson(result, (e) -> new SerializationException("json serialize failed")));
        }
      }
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public void doScanner(String scanPackage) {
    if (StringUtils.isBlank(scanPackage)) {
      scanPackage = this.getClass().getPackageName();
    }
    URL resource = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
    if (Objects.isNull(resource)) {
      return;
    }
    File classPath = new File(resource.getFile());
    for (File file : Objects.requireNonNull(classPath.listFiles())) {
      if (file.isDirectory()) {
        doScanner(scanPackage + "." + file.getName());
      } else {
        if (!file.getName().endsWith(".class")) {
          continue;
        }
        String className = scanPackage + "." + file.getName().replace(".class", "");
        createInstance(className);
      }
    }
  }

  private void createInstance(String className) {
    try {
      Class<?> clazz = Class.forName(className);
      if (clazz.isAnnotationPresent(Controller.class)) {
        Controller annotation = clazz.getAnnotation(Controller.class);
        String beanName = annotation.value();
        if (StringUtils.isBlank(beanName)) {
          beanName = clazz.getName();
        }
        Object instance = clazz.newInstance();
        controllerMaps.put(beanName, instance);
        beanNames.add(beanName);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void initHandlerMethods() {
    if (beanNames.size() == 0) {
      return;
    }
    for (String beanName : beanNames) {
      Object controller = controllerMaps.get(beanName);
      if (controller == null) {
        continue;
      }
      Class<?> controllerClass = controller.getClass();
      String url = "";
      String rootUrl = "";
      if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        rootUrl = requestMapping.value();
      }
      Method[] methods = controllerClass.getMethods();
      for (Method method : methods) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
          RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
          url = "/" + rootUrl + "/" + requestMapping.value();
          url = url.replaceAll("//", "/").replaceAll("/", "/");
          handleMethodInfoMap.put(url, createMethodInfo(url, controller, method));
        }
      }
    }
  }

  private HandleMethodInfo createMethodInfo(String url, Object controller, Method method) {
    Set<HttpMethod> supportedHttpMethods = findSupportedHttpMethods(method);
    return new HandleMethodInfo(url, controller, method, supportedHttpMethods);
  }

  private Set<HttpMethod> findSupportedHttpMethods(Method method) {
    Set<HttpMethod> supportedHttpMethods = new LinkedHashSet<>();
    if (method.isAnnotationPresent(RequestMapping.class)) {
      RequestMapping annotation = method.getAnnotation(RequestMapping.class);
      HttpMethod[] httpMethods = annotation.method();
      Collections.addAll(supportedHttpMethods, httpMethods);
    } else {
      supportedHttpMethods.addAll(asList(HttpMethod.values()));
    }
    return supportedHttpMethods;
  }

  public Map<String, Object> getControllerMaps() {
    return controllerMaps;
  }
}
