package io.github.shawn.deep.in.sc.loadbalancer;

import io.github.shawn.deep.in.sc.loadbalancer.webClient.WebClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LoadBalancerApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoadBalancerApplication.class, args);
  }
}
