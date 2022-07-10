package com.shawn.study.deep.in.java.web.mvc.handle;

import com.shawn.study.deep.in.java.web.mvc.HttpMethod;
import java.lang.reflect.Method;
import java.util.Set;

public class HandleMethodInfo {

  private final String requestPath;
  private final Object controllerClass;
  private final Method method;
  private final Set<HttpMethod> httpMethods;

  public HandleMethodInfo(
      String requestPath, Object controllerClass, Method method, Set<HttpMethod> httpMethods) {
    this.requestPath = requestPath;
    this.controllerClass = controllerClass;
    this.method = method;
    this.httpMethods = httpMethods;
  }

  public String getRequestPath() {
    return requestPath;
  }

  public Object getControllerClass() {
    return controllerClass;
  }

  public Method getMethod() {
    return method;
  }

  public Set<HttpMethod> getHttpMethods() {
    return httpMethods;
  }
}
