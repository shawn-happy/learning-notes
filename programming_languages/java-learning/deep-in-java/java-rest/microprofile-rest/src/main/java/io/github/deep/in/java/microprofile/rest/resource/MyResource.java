package io.github.deep.in.java.microprofile.rest.resource;

import io.github.deep.in.java.microprofile.rest.module.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rest")
public class MyResource {

  private static final List<User> USERS = new ArrayList<>();

  @GET
  @Path("/sayHello")
  @Produces(MediaType.TEXT_PLAIN)
  public String echo() {
    return "JAX-RS First Demo";
  }

  @GET
  @Path("/getUser")
  @Produces(MediaType.APPLICATION_JSON)
  public User get() {
    return new User(1, "shawn");
  }

  @POST
  @Path("/register")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public User createUser(User user) {
    USERS.add(user);
    return user;
  }

  @GET
  @Path("/getUser/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public User getUserById(@PathParam("id") int id) {
    USERS.add(new User(1, "shawn1"));
    USERS.add(new User(2, "shawn2"));
    USERS.add(new User(3, "shawn3"));
    return USERS.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
  }

  @DELETE
  @Path("/delete/{id}")
  public Response deleteById(@PathParam("id") int id) {
    USERS.add(new User(1, "shawn1"));
    USERS.add(new User(2, "shawn2"));
    USERS.add(new User(3, "shawn3"));
    USERS.remove(new User(1, "shawn1"));
    return Response.ok("Delete Ok", MediaType.TEXT_PLAIN).build();
  }

  @PUT
  @Path("/update/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") int id, User newUser) {
    USERS.add(new User(1, "shawn1"));
    USERS.add(new User(2, "shawn2"));
    USERS.add(new User(3, "shawn3"));

    User oldUser = USERS.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    if (oldUser == null) {
      return Response.ok().build();
    }
    oldUser.setName(newUser.getName());
    return Response.ok(oldUser, MediaType.APPLICATION_JSON).build();
  }

  @GET
  @Path("/user/name")
  @Produces(MediaType.APPLICATION_JSON)
  public User getUserByName(@QueryParam("name") String name) {
    return new User(1, name);
  }
}
