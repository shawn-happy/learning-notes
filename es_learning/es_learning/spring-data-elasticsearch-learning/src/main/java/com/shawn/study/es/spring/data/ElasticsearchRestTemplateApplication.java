package com.shawn.study.es.spring.data;

import com.shawn.study.es.spring.data.service.ElasticsearchTemplateService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticsearchRestTemplateApplication {
  public static void main(String[] args) {
    SpringApplication.run(ElasticsearchTemplateService.class, args);
  }
}
