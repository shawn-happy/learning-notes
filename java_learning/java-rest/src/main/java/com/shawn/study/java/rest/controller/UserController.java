package com.shawn.study.java.rest.controller;

import com.shawn.study.java.rest.dto.UserDTO;
import com.shawn.study.java.rest.service.UserService;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserController {

  private final UserService userService = new UserService();

  @GET
  @Path("/query/all")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> getAll() {
    return userService.getUserDTOList();
  }
}
