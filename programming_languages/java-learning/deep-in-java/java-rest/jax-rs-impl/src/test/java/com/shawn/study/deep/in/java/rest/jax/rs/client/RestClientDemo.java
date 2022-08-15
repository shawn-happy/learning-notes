package com.shawn.study.deep.in.java.rest.jax.rs.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.junit.Test;

public class RestClientDemo {

  @Test
  public void test() {
    Client client = ClientBuilder.newClient();
    Response response =
        client
            .target("http://localhost:8080/rest/ping") // WebTarget
            .request() // Invocation.Builder
            .get(); //  Response

    String content = response.readEntity(String.class);

    System.out.println(content);
  }
}
