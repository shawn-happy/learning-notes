package com.shawn.study.java.rest;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

public class UserRuntimeDelegate extends RuntimeDelegate {

  @Override
  public UriBuilder createUriBuilder() {
    return null;
  }

  @Override
  public ResponseBuilder createResponseBuilder() {
    return null;
  }

  @Override
  public VariantListBuilder createVariantListBuilder() {
    return null;
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
  public Builder createLinkBuilder() {
    return null;
  }
}
