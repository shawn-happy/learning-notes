package com.shawn.study.deep.in.java.rest.jax.rs.client;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

public class DefaultRuntimeDelegate extends RuntimeDelegate {

  @Override
  public UriBuilder createUriBuilder() {
    return new DefaultUriBuilder();
  }

  @Override
  public ResponseBuilder createResponseBuilder() {
    return new DefaultResponseBuilder();
  }

  @Override
  public VariantListBuilder createVariantListBuilder() {
    return new DefaultVariantListBuilder();
  }

  @Override
  public <T> T createEndpoint(Application application, Class<T> aClass)
      throws IllegalArgumentException, UnsupportedOperationException {
    return null;
  }

  @Override
  public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> aClass)
      throws IllegalArgumentException {
    return null;
  }

  @Override
  public Builder createLinkBuilder() {
    return null;
  }
}
