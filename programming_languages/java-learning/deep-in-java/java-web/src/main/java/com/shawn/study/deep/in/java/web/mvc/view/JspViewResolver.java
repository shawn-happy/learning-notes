package com.shawn.study.deep.in.java.web.mvc.view;

public class JspViewResolver implements ViewResolver {

  private final String prefix;

  private final String suffix;

  public JspViewResolver(String prefix, String suffix) {
    this.prefix = prefix;
    this.suffix = suffix;
  }

  @Override
  public String resolve(String viewName) {
    return (prefix + viewName + suffix).replaceAll("//", "/");
  }
}
