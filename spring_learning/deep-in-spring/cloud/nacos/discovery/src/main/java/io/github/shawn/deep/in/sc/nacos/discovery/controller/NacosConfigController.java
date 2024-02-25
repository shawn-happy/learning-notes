package io.github.shawn.deep.in.sc.nacos.discovery.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos")
@RefreshScope
public class NacosConfigController {

  @Value("${description}")
  private String value;

  @GetMapping("/config")
  public String getValue() {
    return value;
  }
}
