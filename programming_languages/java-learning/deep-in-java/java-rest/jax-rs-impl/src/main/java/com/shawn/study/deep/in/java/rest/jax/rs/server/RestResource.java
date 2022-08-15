package com.shawn.study.deep.in.java.rest.jax.rs.server;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rest")
public class RestResource {

  /**
   * curl 'http://localhost:8080/rest/ping'
   *
   * @return
   */
  @GET
  @Path("/ping")
  public Response ping() {
    return Response.ok().entity("Service online").build();
  }

  /**
   * curl 'http://localhost:8080/rest/get/1'
   *
   * @param id
   * @return
   */
  @GET
  @Path("/get/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getNotification(@PathParam("id") String id) {
    return Response.ok().entity(new Notification(id, "john", "test notification")).build();
  }

  /**
   * curl -X POST -d '{"id":"23","message":"lorem ipsum","name":"johana"}'
   * http://localhost:8080/rest/post/ --header "Content-Type:application/json"
   *
   * @param notification
   * @return
   */
  @POST
  @Path("/post/")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postNotification(Notification notification) {
    return Response.status(201).entity(notification).build();
  }
}
