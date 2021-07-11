package com.shawn.study.es.rest.client;

import static org.elasticsearch.rest.RestStatus.OK;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.mustache.MultiSearchTemplateResponse;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchApiTests {
  private static final String HOST = "172.27.67.41";
  private static final int PORT = 9200;
  private static final String SCHEMA = "http";
  private static final String INDEX = "products";

  private static final Log log = LogFactory.getLog(SearchApiDemo.class);
  private SearchApiDemo demo;

  @Before
  public void init() {
    demo = new SearchApiDemo(HOST, PORT, SCHEMA);
  }

  @After
  public void destroy() {
    demo.close();
  }

  @Test
  public void test_search_simple_demo() {
    SearchResponse searchResponse = demo.search();
    RestStatus status = searchResponse.status();
    assertEquals(OK, status);
    SearchHits hits = searchResponse.getHits();
    for (SearchHit hit : hits) {
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      assertTrue(sourceAsMap.containsKey("price"));
    }
  }

  @Test
  public void test_search_use_source_builder() {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchQuery("price", 66));
    // page
    searchSourceBuilder.from(0);
    searchSourceBuilder.size(5);
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
    SearchResponse searchResponse = demo.search(searchSourceBuilder);
    long value = searchResponse.getHits().getTotalHits().value;
    assertEquals(1L, value);
  }

  @Test
  public void test_search_match_query_builder() {
    QueryBuilder queryBuilder = new MatchQueryBuilder("price", 66).analyzer("standard");
    SearchResponse searchResponse = demo.search(queryBuilder, 0, 5);
    TimeValue took = searchResponse.getTook();
    log.info(String.format("this request process took %ds", took.getSeconds()));

    SearchHits hits = searchResponse.getHits();
    for (SearchHit hit : hits) {
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      sourceAsMap.forEach((key, value) -> log.info(key + " = " + value));
    }
  }

  @Test
  public void test_multi_search() {
    MultiSearchResponse multiSearchResponse = demo.multiSearch();
    for (MultiSearchResponse.Item item : multiSearchResponse) {
      boolean failure = item.isFailure();
      assertFalse(failure);
      SearchResponse response = item.getResponse();
      for (SearchHit hit : response.getHits()) {
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        sourceAsMap.forEach((key, value) -> log.info(key + " = " + value));
      }
    }
  }

  @Test
  public void test_search_template() {
    SearchTemplateResponse searchTemplateResponse = demo.searchTemplate();
    for (SearchHit hit : searchTemplateResponse.getResponse().getHits()) {
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      sourceAsMap.forEach((key, value) -> log.info(key + " = " + value));
    }
  }

  @Test
  public void test_multi_search_template() {
    MultiSearchTemplateResponse items = demo.multiSearchTemplate();
    for (MultiSearchTemplateResponse.Item item : items) {
      SearchTemplateResponse searchTemplateResponse = item.getResponse();
      for (SearchHit hit : searchTemplateResponse.getResponse().getHits()) {
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        sourceAsMap.forEach((key, value) -> log.info(key + " = " + value));
      }
    }
  }
}
