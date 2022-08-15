package com.shawn.study.deep.in.java.rest.jax.rs.client;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * HTTP GET Method {@link Invocation}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since
 */
class HttpGetInvocation implements Invocation {

  private final URI uri;

  private final URL url;

  private final MultivaluedMap<String, Object> headers;

  HttpGetInvocation(URI uri, MultivaluedMap<String, Object> headers) {
    this.uri = uri;
    this.headers = headers;
    try {
      this.url = uri.toURL();
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Invocation property(String name, Object value) {
    return this;
  }

  @Override
  public Response invoke() {
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(HttpMethod.GET);
      setRequestHeaders(connection);
      // TODO Set the cookies
      int statusCode = connection.getResponseCode();
      //            Response.ResponseBuilder responseBuilder = Response.status(statusCode);
      //
      //            responseBuilder.build();
      DefaultResponse response = new DefaultResponse();
      response.setConnection(connection);
      response.setStatus(statusCode);
      return response;
      //            Response.Status status = Response.Status.fromStatusCode(statusCode);
      //            switch (status) {
      //                case Response.Status.OK:
      //
      //                    break;
      //                default:
      //                    break;
      //            }

    } catch (IOException e) {
      // TODO Error handler
    }
    return null;
  }

  private void setRequestHeaders(HttpURLConnection connection) {
    for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
      String headerName = entry.getKey();
      for (Object headerValue : entry.getValue()) {
        connection.setRequestProperty(headerName, headerValue.toString());
      }
    }
  }

  @Override
  public <T> T invoke(Class<T> responseType) {
    Response response = invoke();
    return response.readEntity(responseType);
  }

  @Override
  public <T> T invoke(GenericType<T> responseType) {
    Response response = invoke();
    return response.readEntity(responseType);
  }

  @Override
  public Future<Response> submit() {
    return null;
  }

  @Override
  public <T> Future<T> submit(Class<T> responseType) {
    return null;
  }

  @Override
  public <T> Future<T> submit(GenericType<T> responseType) {
    return null;
  }

  @Override
  public <T> Future<T> submit(InvocationCallback<T> callback) {
    return null;
  }
}
