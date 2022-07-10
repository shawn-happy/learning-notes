package com.shawn.study.deep.in.java.web.servlet;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class ServletRequestDemo {

  private static final Logger log =
      LogManager.getLogManager().getLogger(ServletRequestDemo.class.getName());

  public static void parameters(HttpServletRequest request) {
    Map<String, String[]> parameterMap = request.getParameterMap();
    Enumeration<String> parameterNames = request.getParameterNames();
    parameterMap.forEach(
        (name, value) -> {
          String parameter = request.getParameter(name);
          String[] parameterValues = request.getParameterValues(name);
          while (parameterNames.hasMoreElements()) {
            String nextElement = parameterNames.nextElement();
            if (nextElement.equals(name)
                && null != parameterValues
                && parameterValues.length != 0) {
              log.info(
                  String.format(
                      "parameter name: %s, parameter values from request.getParameterValues(name): %s, "
                          + "parameter values from request.getParameterMap.get(name): %s, value of parameters: %s",
                      name, Arrays.toString(parameterValues), Arrays.toString(value), parameter));
            }
          }
        });
  }

  public static void pathInfo(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    String servletPath = request.getServletPath();
    String contextPath = request.getContextPath();
    log.info(
        String.format(
            "pathInfo: %s, servletPath: %s, contextPath: %s", pathInfo, servletPath, contextPath));

    String requestURI = request.getRequestURI();
    String requestURL = request.getRequestURL().toString();
    log.info(String.format("requestURI: %s, requestURL: %s", requestURI, requestURL));

    String pathTranslated = request.getPathTranslated();
    log.info(String.format("pathTranslated: %s", pathTranslated));

    String realPath = request.getRealPath("index.jsp");
    log.info(String.format("index.jsp real path: %s", realPath));
  }

  public static void fileUpload(HttpServletRequest request) throws Exception {
    Part file = request.getPart("file");
    String fileName = file.getSubmittedFileName();
    for (Part part : request.getParts()) {
      part.write("/tmp/" + fileName);
    }
  }

  public static void attribute(HttpServletRequest request) {
    Enumeration<String> attributeNames = request.getAttributeNames();
    while (attributeNames.hasMoreElements()) {
      String name = attributeNames.nextElement();
      Object value = request.getAttribute(name);
      log.info(String.format("attribute name: %s, attribute value: %s", name, value));
    }
  }

  public static void headers(HttpServletRequest request) {
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      String header = request.getHeader(name);
      Enumeration<String> headers = request.getHeaders(name);
      log.info(
          String.format(
              "header name: %s, header value: %s, header values: %s", name, header, headers));
    }
  }

  public static void cookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      log.info(
          String.format("cookie name: %s, cookie value: %s", cookie.getName(), cookie.getValue()));
    }
  }

  public static void contentType(HttpServletRequest request) {
    log.info(String.format("request content type: %s", request.getContentType()));
  }
}
