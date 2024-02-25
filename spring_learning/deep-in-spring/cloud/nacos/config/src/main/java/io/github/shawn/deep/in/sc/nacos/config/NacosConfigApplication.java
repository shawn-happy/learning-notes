package io.github.shawn.deep.in.sc.nacos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NacosConfigApplication implements CommandLineRunner {

  @Value("${description}")
  private String value;

  public static void main(String[] args) {
    SpringApplication.run(NacosConfigApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.printf("获取到的值为：%s\n", value);
  }
}
