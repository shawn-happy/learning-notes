package com.shawn.study.es.rest.client;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.MultiSearchTemplateRequest;
import org.elasticsearch.script.mustache.MultiSearchTemplateResponse;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * elasticsearch search APIs Demo
 *
 * @see <a
 *     href="https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.9/_search_apis.html">
 *     search APIs </a>
 * @author Shawn
 * @since 1.0.0
 */
public class SearchApiDemo {

  private final RestHighLevelClient client;

  public SearchApiDemo(String host, int port, String schema) {
    client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, schema)));
  }

  public void close() {
    ThrowableAction.execute(client::close);
  }

  public SearchResponse search() {
    SearchRequest searchRequest = new SearchRequest();
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    searchRequest.indices("products");
    searchRequest.source(searchSourceBuilder);
    return ThrowableFunction.execute(
        searchRequest, request -> client.search(searchRequest, RequestOptions.DEFAULT));
  }

  public SearchResponse search(SearchSourceBuilder searchSourceBuilder) {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.source(searchSourceBuilder);
    return ThrowableFunction.execute(
        searchRequest, request -> client.search(searchRequest, RequestOptions.DEFAULT));
  }

  public SearchResponse search(QueryBuilder queryBuilder, int from, int size) {
    SearchRequest searchRequest = new SearchRequest();
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(queryBuilder);
    searchSourceBuilder.from(from);
    searchSourceBuilder.size(size);
    searchRequest.source(searchSourceBuilder);
    return ThrowableFunction.execute(
        searchRequest, request -> client.search(searchRequest, RequestOptions.DEFAULT));
  }

  public MultiSearchResponse multiSearch() {
    MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
    SearchRequest firstSearchRequest = new SearchRequest();
    firstSearchRequest.indices("users");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchQuery("username", "jackson"));
    firstSearchRequest.source(searchSourceBuilder);
    multiSearchRequest.add(firstSearchRequest);
    SearchRequest secondSearchRequest = new SearchRequest();
    searchSourceBuilder = new SearchSourceBuilder();
    secondSearchRequest.indices("users");
    searchSourceBuilder.query(QueryBuilders.matchQuery("username", "John"));
    secondSearchRequest.source(searchSourceBuilder);
    multiSearchRequest.add(secondSearchRequest);
    return ThrowableFunction.execute(
        multiSearchRequest, request -> client.msearch(request, RequestOptions.DEFAULT));
  }

  public SearchTemplateResponse searchTemplate() {
    return ThrowableFunction.execute(
        toSearchTemplateRequest("jackson"),
        request -> client.searchTemplate(request, RequestOptions.DEFAULT));
  }

  public MultiSearchTemplateResponse multiSearchTemplate() {
    String[] searchTerms = {"jackson", "jack", "bob"};

    MultiSearchTemplateRequest multiSearchTemplateRequest = new MultiSearchTemplateRequest();
    for (String searchTerm : searchTerms) {
      multiSearchTemplateRequest.add(toSearchTemplateRequest(searchTerm));
    }
    return ThrowableFunction.execute(
        multiSearchTemplateRequest,
        request -> client.msearchTemplate(request, RequestOptions.DEFAULT));
  }

  private SearchTemplateRequest toSearchTemplateRequest(String username) {
    SearchTemplateRequest request = new SearchTemplateRequest();
    request.setRequest(new SearchRequest("users"));

    request.setScriptType(ScriptType.INLINE);
    request.setScript(
        "{"
            + "  \"query\": { \"match\" : { \"{{field}}\" : \"{{value}}\" } },"
            + "  \"size\" : \"{{size}}\""
            + "}");

    Map<String, Object> scriptParams = new HashMap<>();
    scriptParams.put("field", "username");
    scriptParams.put("value", username);
    scriptParams.put("size", 5);
    request.setScriptParams(scriptParams);
    return request;
  }
}
