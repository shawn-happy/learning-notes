package com.shawn.study.es.spring.data.service;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * {@link ElasticsearchRestTemplate} demo
 *
 * @author Shawn
 * @since 1.0.0
 */
@Service
public class ElasticsearchTemplateService {

  private final ElasticsearchRestTemplate template;
  private final IndexCoordinates indexCoordinates;
  private final IndexOperations indexOperations;

  public ElasticsearchTemplateService(
      ElasticsearchRestTemplate template,
      IndexCoordinates indexCoordinates,
      IndexOperations indexOperations) {
    this.template = template;
    this.indexCoordinates = indexCoordinates;
    this.indexOperations = indexOperations;
  }

  /**
   * create index
   *
   * @param settings index settings
   * @param mappings index mappings
   * @return boolean
   */
  public boolean createIndex(Map<String, Object> settings, Document mappings) {
    return indexOperations.create(settings, mappings);
  }

  /**
   * check if this index exists
   *
   * @return boolean
   */
  public boolean existsIndex() {
    return indexOperations.exists();
  }

  /**
   * get elasticsearch index setting
   *
   * @return {@link Settings}
   */
  public Settings getSettings() {
    return indexOperations.getSettings();
  }

  /**
   * get elasticsearch index mapping
   *
   * @return Map
   */
  public Map<String, Object> getMappings() {
    return indexOperations.getMapping();
  }

  /**
   * update index mapping
   *
   * @param mapping index mappings
   * @return boolean
   */
  public boolean updateIndex(Document mapping) {
    return indexOperations.putMapping(mapping);
  }

  /**
   * delete index
   *
   * @return boolean
   */
  public boolean deleteIndex() {
    return indexOperations.delete();
  }
}
