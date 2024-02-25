package io.github.shawn.deep.in.sc.nacos.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosDiscoveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(NacosDiscoveryApplication.class, args);
  }
}
