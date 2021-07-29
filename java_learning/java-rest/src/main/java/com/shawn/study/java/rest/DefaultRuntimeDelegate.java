package com.shawn.study.java.rest;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant;
import javax.ws.rs.ext.RuntimeDelegate;

public class DefaultRuntimeDelegate extends RuntimeDelegate {

  @Override
  public UriBuilder createUriBuilder() {
    return new DefaultUriBuilder();
  }

  @Override
  public Response.ResponseBuilder createResponseBuilder() {
    return new DefaultResponseBuilder();
  }

  @Override
  public Variant.VariantListBuilder createVariantListBuilder() {
    return new DefaultVariantListBuilder();
  }

  @Override
  public <T> T createEndpoint(Application application, Class<T> endpointType)
      throws IllegalArgumentException, UnsupportedOperationException {
    return null;
  }

  @Override
  public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) throws IllegalArgumentException {
    return null;
  }

  @Override
  public Link.Builder createLinkBuilder() {
    return null;
  }
}
