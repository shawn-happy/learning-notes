package io.github.shawn.deep.in.sc.loadbalancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class LoadBalancerApplication implements CommandLineRunner {

  @Autowired private RestTemplate restTemplate;

  public static void main(String[] args) {
    SpringApplication.run(LoadBalancerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println(restTemplate.getForObject("http://nacos-config/nacos/config", String.class));
  }
}
