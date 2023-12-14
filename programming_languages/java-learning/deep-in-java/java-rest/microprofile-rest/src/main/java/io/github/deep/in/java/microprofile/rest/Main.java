package io.github.deep.in.java.microprofile.rest;

import io.github.deep.in.java.microprofile.rest.application.MyApplication;
import io.github.deep.in.java.microprofile.rest.resource.MyResource;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static final String BASE_URI = "http://localhost:8080/";

  public static HttpServer startServer() {
    // scan packages
    final ResourceConfig config = new ResourceConfig();
    config.register(MyApplication.class);
    config.register(MyResource.class);
    config.packages("com.fasterxml.jackson.jaxrs.json");
    String applicationName = config.getApplicationName();
    System.out.printf("start server, application name: %s%n", applicationName);
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
  }

  public static void main(String[] args) {
    HttpServer httpServer = Main.startServer();

    try {
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
                      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
                    }
                  }));

      System.out.printf("Application started.%nStop the application using CTRL+C%n");

      // block and wait shut down signal, like CTRL+C
      Thread.currentThread().join();

    } catch (InterruptedException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
