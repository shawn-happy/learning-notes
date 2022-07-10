package com.shawn.study.deep.in.java.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CharsetEncodingFilter implements Filter {

  private String encode;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    encode = filterConfig.getInitParameter("encoding");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      httpServletRequest.setCharacterEncoding(encode);
      httpServletResponse.setCharacterEncoding(encode);
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {}
}
