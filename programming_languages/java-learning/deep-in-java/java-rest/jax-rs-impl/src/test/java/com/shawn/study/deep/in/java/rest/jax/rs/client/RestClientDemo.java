package com.shawn.study.deep.in.java.rest.jax.rs.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.junit.Test;

public class RestClientDemo {

  @Test
  public void test() {
    Client client = ClientBuilder.newClient();
    Response response =
        client
            .target("http://localhost:8080/api/rest/ping") // WebTarget
            .request() // Invocation.Builder
            .get(); //  Response

    String content = response.readEntity(String.class);

    System.out.println(content);
  }
}
