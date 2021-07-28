package com.shawn.study.java.rest;

import static com.shawn.study.java.rest.util.PathUtil.buildPath;
import static com.shawn.study.java.rest.util.PathUtil.resolvePath;

import com.shawn.study.java.rest.util.URLUtil;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

public class DefaultUriBuilder extends UriBuilder {

  private int port;
  private String host;
  private String path;
  private String query;
  private String scheme;
  private String fragment;
  private String userInfo;
  private String uriTemplate;
  private String schemeSpecificPart;

  private MultivaluedMap<String, String> matrixParams = new MultivaluedHashMap<>();

  private MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();

  @Override
  public UriBuilder clone() {
    return new DefaultUriBuilder(this);
  }

  public DefaultUriBuilder() {}

  private DefaultUriBuilder(DefaultUriBuilder builder) {
    this.port = builder.port;
    this.host = builder.host;
    this.path = builder.path;
    this.scheme = builder.scheme;
    this.fragment = builder.fragment;
    this.userInfo = builder.userInfo;
    this.schemeSpecificPart = builder.schemeSpecificPart;
  }

  @Override
  public UriBuilder uri(URI uri) {
    scheme = uri.getScheme();
    userInfo = uri.getRawUserInfo();
    host = uri.getHost();
    port = uri.getPort();
    path = uri.getPath();
    schemeSpecificPart = uri.getSchemeSpecificPart();
    fragment = uri.getFragment();
    query = uri.getRawQuery();
    queryParams.clear();
    queryParams.putAll(Objects.requireNonNull(URLUtil.resolveParameters(query)));
    return null;
  }

  @Override
  public UriBuilder uri(String uriTemplate) {
    this.uriTemplate = uriTemplate;
    return this;
  }

  @Override
  public UriBuilder scheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  @Override
  public UriBuilder schemeSpecificPart(String ssp) {
    schemeSpecificPart = ssp;
    return this;
  }

  @Override
  public UriBuilder userInfo(String ui) {
    userInfo = ui;
    return this;
  }

  @Override
  public UriBuilder host(String host) {
    this.host = host;
    return this;
  }

  @Override
  public UriBuilder port(int port) {
    this.port = port;
    return this;
  }

  @Override
  public UriBuilder replacePath(String path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UriBuilder path(String path) {
    this.path = buildPath(this.path, path);
    return this;
  }

  @Override
  public UriBuilder path(Class resource) {
    return path(resolvePath(resource));
  }

  @Override
  public UriBuilder path(Class resource, String method) {
    return path(resolvePath(resource, method));
  }

  @Override
  public UriBuilder path(Method method) {
    return path(resolvePath(method.getDeclaringClass(), method));
  }

  @Override
  public UriBuilder segment(String... segments) {
    this.path = buildPath(path, segments);
    return this;
  }

  @Override
  public UriBuilder replaceMatrix(String matrix) {
    return null;
  }

  @Override
  public UriBuilder matrixParam(String name, Object... values) {

    return null;
  }

  @Override
  public UriBuilder replaceMatrixParam(String name, Object... values) {
    return null;
  }

  @Override
  public UriBuilder replaceQuery(String query) {
    return null;
  }

  @Override
  public UriBuilder queryParam(String name, Object... values) {
    return null;
  }

  @Override
  public UriBuilder replaceQueryParam(String name, Object... values) {
    return null;
  }

  @Override
  public UriBuilder fragment(String fragment) {
    this.fragment = fragment;
    return this;
  }

  @Override
  public UriBuilder resolveTemplate(String name, Object value) {
    return null;
  }

  @Override
  public UriBuilder resolveTemplate(String name, Object value, boolean encodeSlashInPath) {
    return null;
  }

  @Override
  public UriBuilder resolveTemplateFromEncoded(String name, Object value) {
    return null;
  }

  @Override
  public UriBuilder resolveTemplates(Map<String, Object> templateValues) {
    return null;
  }

  @Override
  public UriBuilder resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath)
      throws IllegalArgumentException {
    return null;
  }

  @Override
  public UriBuilder resolveTemplatesFromEncoded(Map<String, Object> templateValues) {
    return null;
  }

  @Override
  public URI buildFromMap(Map<String, ?> values) {
    return null;
  }

  @Override
  public URI buildFromMap(Map<String, ?> values, boolean encodeSlashInPath)
      throws IllegalArgumentException, UriBuilderException {
    return null;
  }

  @Override
  public URI buildFromEncodedMap(Map<String, ?> values)
      throws IllegalArgumentException, UriBuilderException {
    return null;
  }

  @Override
  public URI build(Object... values) throws IllegalArgumentException, UriBuilderException {
    return null;
  }

  @Override
  public URI build(Object[] values, boolean encodeSlashInPath)
      throws IllegalArgumentException, UriBuilderException {
    return null;
  }

  @Override
  public URI buildFromEncoded(Object... values)
      throws IllegalArgumentException, UriBuilderException {
    return null;
  }

  @Override
  public String toTemplate() {
    return null;
  }
}
