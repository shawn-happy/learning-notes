package com.shawn.study.java.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
public class DefaultProvider implements MessageBodyReader<String>, MessageBodyWriter<String> {

  @Override
  public boolean isReadable(
      Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return String.class.equals(type);
  }

  @Override
  public String readFrom(
      Class<String> type,
      Type genericType,
      Annotation[] annotations,
      MediaType mediaType,
      MultivaluedMap<String, String> httpHeaders,
      InputStream entityStream)
      throws IOException, WebApplicationException {
    if (noContent(httpHeaders)) {
      return "";
    }
    return null;
  }

  @Override
  public boolean isWriteable(
      Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return String.class.equals(type);
  }

  @Override
  public long getSize(
      String string,
      Class<?> type,
      Type genericType,
      Annotation[] annotations,
      MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(
      String o,
      Class<?> type,
      Type genericType,
      Annotation[] annotations,
      MediaType mediaType,
      MultivaluedMap<String, Object> httpHeaders,
      OutputStream entityStream)
      throws IOException, WebApplicationException {
    String charset = mediaType.getParameters().get("charset");
    if (charset == null) entityStream.write(o.getBytes());
    else entityStream.write(o.getBytes(charset));
  }

  private boolean noContent(MultivaluedMap<String, String> httpHeaders) {
    if (httpHeaders == null) return false;
    String contentLength = (String) httpHeaders.getFirst(HttpHeaders.CONTENT_LENGTH);
    if (contentLength != null) {
      int length = Integer.parseInt(contentLength);
      if (length == 0) return true;
    }
    return false;
  }
}
