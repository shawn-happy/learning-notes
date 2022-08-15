package com.shawn.study.deep.in.java.rest.jax.rs.server;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class MainApp {

  private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());

  // we start at port 8080
  public static final String BASE_URI = "http://localhost:8080/";

  // Starts Grizzly HTTP server
  public static HttpServer startServer() {

    // scan packages
    final ResourceConfig config = new ResourceConfig();
    config.register(SimpleApplication.class);
    config.register(RestResource.class);
    config.packages("com.fasterxml.jackson.jaxrs.json");
    LOGGER.info("Starting Server........");

    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
  }

  public static void main(String[] args) {

    try {

      final HttpServer httpServer = startServer();

      // add jvm shutdown hook
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(
                  () -> {
                    try {
                      System.out.println("Shutting down the application...");

                      httpServer.shutdownNow();

                      System.out.println("Done, exit.");
                    } catch (Exception e) {
                      Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, e);
                    }
                  }));

      System.out.printf("Application started.%nStop the application using CTRL+C%n");

      // block and wait shut down signal, like CTRL+C
      Thread.currentThread().join();

    } catch (InterruptedException ex) {
      Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
