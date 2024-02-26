package io.github.shawn.deep.in.sc.loadbalancer.resttemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/lb/resttemplate")
public class RestTemplateController {

  private final RestTemplate restTemplate;

  public RestTemplateController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/getValue")
  public String getValue() {
    return restTemplate.getForObject("http://nacos-config/nacos/config", String.class);
  }
}
