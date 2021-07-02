package com.shawn.study.java.web.servlet;

import static java.util.Arrays.asList;

import com.shawn.study.java.web.HandlerMethodInfo;
import com.shawn.study.java.web.HttpMethod;
import com.shawn.study.java.web.JsonUtil;
import com.shawn.study.java.web.annotation.Controller;
import com.shawn.study.java.web.annotation.RequestBody;
import com.shawn.study.java.web.annotation.RequestMapping;
import com.shawn.study.java.web.annotation.ResponseBody;
import com.shawn.study.java.web.view.JspViewResolver;
import com.shawn.study.java.web.view.ViewResolver;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
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

/**
 * @author Shawn
 * @since 1.0
 */
public class DispatcherServlet extends HttpServlet {

  /** ioc container */
  private final Map<String, Object> beanMappings = new ConcurrentHashMap<>();

  /** 存储URL对应的方法 */
  private final Map<String, HandlerMethodInfo> handlerMethodMappings = new ConcurrentHashMap<>();

  /** 存储URL对应的Bean */
  private final Map<String, Object> controllerMappings = new ConcurrentHashMap<>();

  private final ViewResolver viewResolver = new JspViewResolver("/", ".jsp");

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doDispatcher(request, response);
  }

  @Override
  public void init() throws ServletException {
    // 扫包
    String scanPackage = "com.shawn.study.java.web.controller";
    doScanner(scanPackage);
    // 处理requestMapping
    initHandlerMethod();
  }

  private void doDispatcher(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String url = request.getRequestURI();
    String contextPath = request.getContextPath();
    url = url.replace(contextPath, "").replaceAll("/+", "/").replaceAll("//", "/");
    HandlerMethodInfo methodInfo = handlerMethodMappings.get(url);
    Object controller = controllerMappings.get(url);
    PrintWriter writer = response.getWriter();
    if (Objects.isNull(methodInfo) || Objects.isNull(controller)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    Set<HttpMethod> supportedHttpMethods = methodInfo.getSupportedHttpMethods();
    // 判断是不是该方法支持的请求方式
    if (!supportedHttpMethods.contains(HttpMethod.toHttpMethod(request.getMethod()))) {
      response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      return;
    }
    Method method = methodInfo.getMethod();

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
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
          InputStream inputStream = request.getInputStream();
          if (inputStream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
              stringBuilder.append(charBuffer, 0, bytesRead);
            }
          } else {
            stringBuilder.append("");
          }
        } catch (IOException ex) {
          throw ex;
        } finally {
          if (bufferedReader != null) {
            try {
              bufferedReader.close();
            } catch (IOException ex) {
              throw ex;
            }
          }
        }
        Class<?> type = parameter.getType();
        params[i] = JsonUtil.readValue(stringBuilder.toString(), type);

      } else if (parameter.getAnnotations().length == 0) {
        params[i] = methodParameter.get(parameter.getName());
      }
    }

    // 利用反射机制来调用
    try {

      Object result = method.invoke(controller, params);
      if (Objects.isNull(result)) {
        log("result is null");
      } else {
        // 如果标记为forward，则执行forward请求
        if (!method.isAnnotationPresent(ResponseBody.class)) {
          request
              .getRequestDispatcher(viewResolver.resolve(result.toString()))
              .forward(request, response);
        } else {
          writer = response.getWriter();
          writer.write(
              JsonUtil.toJson(result, (e) -> new SerializationException("json serialize failed")));
        }
      }
    } catch (Exception e) {
      throw new ServletException(e);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  private void doScanner(String scanPackage) {
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
        beanMappings.put(beanName, instance);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initHandlerMethod() {
    Set<String> beanNames = beanMappings.keySet();
    for (String beanName : beanNames) {
      Class<?> controller = beanMappings.get(beanName).getClass();
      if (!(controller.isAnnotationPresent(Controller.class))) {
        continue;
      }
      String url = "";
      String rootUrl = "";
      if (controller.isAnnotationPresent(RequestMapping.class)) {
        RequestMapping requestMapping = controller.getAnnotation(RequestMapping.class);
        rootUrl = requestMapping.value();
      }

      for (Method method : controller.getMethods()) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
          RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
          url = "/" + rootUrl + "/" + requestMapping.value();
          url = url.replaceAll("//", "/").replaceAll("/", "/");
          handlerMethodMappings.put(url, createMethodInfo(url, method));
          controllerMappings.put(url, beanMappings.get(beanName));
        }
      }
    }
  }

  private HandlerMethodInfo createMethodInfo(String url, Method method) {
    Set<HttpMethod> supportedHttpMethods = findSupportedHttpMethods(method);
    return new HandlerMethodInfo(url, method, supportedHttpMethods);
  }

  private Set<HttpMethod> findSupportedHttpMethods(Method method) {
    Set<HttpMethod> supportedHttpMethods = new LinkedHashSet<>();
    if (method.isAnnotationPresent(RequestMapping.class)) {
      RequestMapping annotation = method.getAnnotation(RequestMapping.class);
      HttpMethod httpMethod = annotation.method();
      supportedHttpMethods.add(httpMethod);
    } else {
      supportedHttpMethods.addAll(asList(HttpMethod.values()));
    }
    return supportedHttpMethods;
  }
}
