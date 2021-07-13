package com.shawn.study.es.spring.data.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

  @Override
  @Bean
  public RestHighLevelClient elasticsearchClient() {
    final ClientConfiguration clientConfiguration =
        ClientConfiguration.builder().connectedTo("172.27.67.41:9200").build();
    return RestClients.create(clientConfiguration).rest();
  }

  @Bean
  public ElasticsearchRestTemplate elasticsearchRestTemplate() {
    return new ElasticsearchRestTemplate(elasticsearchClient());
  }

  @Bean
  public IndexCoordinates indexCoordinates() {
    return IndexCoordinates.of("spring-data-es-demo");
  }

  @Bean
  public IndexOperations indexOperations() {
    return elasticsearchRestTemplate().indexOps(indexCoordinates());
  }
}
