package com.shawn.study.java.rest;

import static org.junit.Assert.assertEquals;

import com.shawn.study.java.rest.provider.DefaultProvider;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class JaxRsSimpleDemo {

  @Test
  public void test_rest_client() {
    Client client = ClientBuilder.newClient();
    Response response =
        client
            .target("http://localhost:8080/demo/hello?who=shawn")
            .request(MediaType.TEXT_PLAIN)
            .get();
    int status = response.getStatus();
    assertEquals(200, status);
    String s = response.readEntity(String.class);
    assertEquals("Hello, shawn!", s);
  }

  @Test
  public void test_rest_client_target() {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:8080");
    WebTarget webTarget = target.path("demo").path("hello").queryParam("who", "shawn");
    Response response = webTarget.request(MediaType.TEXT_PLAIN).get();
    int status = response.getStatus();
    assertEquals(200, status);
    String s = response.readEntity(String.class);
    assertEquals("Hello, shawn!", s);
  }

  @Test
  public void test_rest_client_target_template() {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:8080");
    // http://localhost:8080/demo/hello/{who}
    WebTarget webTarget = target.path("demo").path("hello").path("{who}");
    Response response =
        webTarget.resolveTemplate("who", "shawn").request(MediaType.TEXT_PLAIN).get();
    int status = response.getStatus();
    assertEquals(200, status);
    String s = response.readEntity(String.class);
    assertEquals("Hello, shawn!", s);
  }

  @Test
  public void test_rest_provider() {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:8080");
    // http://localhost:8080/demo/hello/{who}
    WebTarget webTarget = target.path("demo").path("hello").path("{who}");
    WebTarget register = webTarget.register(DefaultProvider.class);
    Response response =
        register.resolveTemplate("who", "shawn").request(MediaType.TEXT_PLAIN).get();
    int status = response.getStatus();
    assertEquals(200, status);
    String s = response.readEntity(String.class);
    assertEquals("Hello, shawn!", s);
  }

  @Test
  public void test_url_connect() {
    try {
      URL url = new URL("http://localhost:8080/demo/hello?who=shawn");
      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
      httpURLConnection.setRequestMethod("GET");
      httpURLConnection.connect();
      try (InputStream inputStream = httpURLConnection.getInputStream()) {
        String s = IOUtils.toString(inputStream, "UTF-8");
        assertEquals("Hello, shawn!", s);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
