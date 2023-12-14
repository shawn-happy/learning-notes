package io.github.deep.in.java.microprofile.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.github.deep.in.java.microprofile.rest.module.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MyResourceTests {

  private HttpServer server;
  private WebTarget target;

  @BeforeEach
  public void setUp() throws Exception {
    server = Main.startServer();
    Client c = ClientBuilder.newClient();
    target = c.target(Main.BASE_URI);
  }

  @AfterEach
  public void tearDown() throws Exception {
    server.stop();
  }

  /** Test to see that the message "Got it!" is sent in the response. */
  @Test
  public void testGetIt() {
    String responseMsg = target.path("/rest/sayHello").request().get(String.class);
    assertEquals("JAX-RS First Demo", responseMsg);
  }

  @Test
  public void testGetUser() {
    User responseMsg = target.path("/rest/getUser").request().get(User.class);
    assertNotNull(responseMsg);
    assertEquals(1, responseMsg.getId());
  }

  @Test
  public void testCreateUser() {
    User response =
        target
            .path("/rest/register")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(new User(1, "shawn"), MediaType.APPLICATION_JSON), User.class);
    assertNotNull(response);
  }

  @Test
  public void testGetUserById() {
    User responseMsg = target.path("/rest/getUser/2").request().get(User.class);
    assertNotNull(responseMsg);
    assertEquals(2, responseMsg.getId());
    assertEquals("shawn2", responseMsg.getName());

    responseMsg = target.path("/rest/getUser/4").request().get(User.class);
    assertNull(responseMsg);
  }

  @Test
  public void testDeleteUserById() {
    Response response = target.path("/rest/delete/2").request().delete();
    assertNotNull(response);
    String s = response.readEntity(String.class);
    assertEquals("Delete Ok", s);

    response = target.path("/rest/delete/4").request().delete();
    assertNotNull(response);
    s = response.readEntity(String.class);
    assertEquals("Delete Ok", s);
  }

  @Test
  public void testUpdateUserById() {
    Response response =
        target
            .path("/rest/update/2")
            .request()
            .put(Entity.entity(new User(2, "2-shawn"), MediaType.APPLICATION_JSON));
    assertNotNull(response);
    User user = response.readEntity(User.class);
    assertEquals("2-shawn", user.getName());

    response =
        target
            .path("/rest/update/4")
            .request()
            .put(Entity.entity(new User(4, "4-shawn"), MediaType.APPLICATION_JSON));
    assertNotNull(response);
  }

  @Test
  public void testGetUserName() {
    User user =
        target.path("/rest/user/name").queryParam("name", "shawn").request().get(User.class);
    assertNotNull(user);
    assertEquals("shawn", user.getName());
  }
}
