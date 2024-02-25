package io.github.shawn.deep.in.sc.nacos.consumer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/nacos/consumer")
public class NacosConsumerController {

  private final RestTemplate restTemplate;

  public NacosConsumerController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/value")
  public String getValue() {
    return restTemplate.getForObject("http://nacos-config/nacos/config", String.class);
  }
}
