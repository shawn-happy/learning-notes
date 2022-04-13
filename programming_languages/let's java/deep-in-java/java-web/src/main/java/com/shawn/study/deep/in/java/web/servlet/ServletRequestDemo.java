package com.shawn.study.deep.in.java.web.servlet;

import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServletRequestDemo {

  private static final Logger log = LogManager.getLogger(ServletRequestDemo.class);

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
                  "parameter name: {}, parameter values from request.getParameterValues(name): {}, "
                      + "parameter values from request.getParameterMap.get(name): {}, value of parameters: {}",
                  name,
                  parameterValues,
                  value,
                  parameter);
            }
          }
        });
  }

  public static void pathInfo(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    String servletPath = request.getServletPath();
    String contextPath = request.getContextPath();
    log.info("pathInfo: {}, servletPath: {}, contextPath: {}", pathInfo, servletPath, contextPath);

    String requestURI = request.getRequestURI();
    String requestURL = request.getRequestURL().toString();
    log.info("requestURI: {}, requestURL: {}", requestURI, requestURL);

    String pathTranslated = request.getPathTranslated();
    log.info("pathTranslated: {}", pathTranslated);

    String realPath = request.getRealPath("index.jsp");
    log.info("index.jsp real path: {}", realPath);
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
      log.info("attribute name: {}, attribute value: {}", name, value);
    }
  }

  public static void headers(HttpServletRequest request) {
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      String header = request.getHeader(name);
      Enumeration<String> headers = request.getHeaders(name);
      log.info("header name: {}, header value: {}, header values: {}", name, header, headers);
    }
  }

  public static void cookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      log.info("cookie name: {}, cookie value: {}", cookie.getName(), cookie.getValue());
    }
  }

  public static void contentType(HttpServletRequest request) {
    log.info("request content type: {}", request.getContentType());
  }
}
