package com.shawn.study.deep.in.java.rest.jax.rs.client;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.Variant;
import jakarta.ws.rs.ext.RuntimeDelegate;

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
