## elasticsearch client APIs

### Java REST Client

#### maven dependencies

```xml
  <properties>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>

    <elasticsearch.client.version>7.9.3</elasticsearch.client.version>
    <junit.version>4.13.1</junit.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.elasticsearch.client</groupId>
      <artifactId>elasticsearch-rest-client</artifactId>
      <version>${elasticsearch.client.version}</version>
    </dependency>
    <dependency>
      <groupId>org.elasticsearch.client</groupId>
      <artifactId>elasticsearch-rest-high-level-client</artifactId>
      <version>${elasticsearch.client.version}</version>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>${junit.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
```

#### Get Started

```java
public RestHighLevelClient connect(String host, int port, String schema) {
    return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, schema)));
}

@Test
public void test_rest_high_client() throws Exception {
    ElasticSearchRestDemo demo = new ElasticSearchRestDemo();
    RestHighLevelClient client = demo.connect("localhost", 9200, "http");
    IndicesClient indices = client.indices();
    GetIndexRequest indexRequest = new GetIndexRequest("movies");
    GetIndexResponse getIndexResponse = indices.get(indexRequest, RequestOptions.DEFAULT);
    int length = getIndexResponse.getIndices().length;
    Assert.assertEquals(1, length);
    client.close();
}
```

#### index API

##### Create Index

```java
public Settings settings(Settings.Builder builder) {
    return builder.build();
}

public XContentBuilder jsonContentBuilder(Map<String, String> fieldTypeMap) {
    try {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject();
        {
            xContentBuilder.startObject("properties");
            {
                fieldTypeMap.forEach(
                    (key, value) -> {
                        try {
                            xContentBuilder.startObject(key);
                            {
                                xContentBuilder.field("type", value);
                            }
                            xContentBuilder.endObject();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            }
            xContentBuilder.endObject();
        }
        xContentBuilder.endObject();
        return xContentBuilder;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

public CreateIndexResponse createIndex(
    String index, Settings settings, XContentBuilder xContentBuilder) {
  CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
  createIndexRequest.settings(settings);
  createIndexRequest.mapping(xContentBuilder);
  try {
    return client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}

@Test
public void test_create_index() {
    Settings settings =
        demo.settings(
        Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 0));
    Map<String, String> fieldTypeMap = new HashMap<>();
    fieldTypeMap.put("username", "text");
    fieldTypeMap.put("age", "long");
    fieldTypeMap.put("password", "text");
    fieldTypeMap.put("address", "text");
    XContentBuilder xContentBuilder = demo.jsonContentBuilder(fieldTypeMap);
    CreateIndexResponse response = demo.createIndex(INDEX, settings, xContentBuilder);
    Assert.assertTrue(response.isAcknowledged());
}
```

##### Get Index

```java
public GetIndexResponse getIndex(String index) {
  GetIndexRequest getIndexRequest = new GetIndexRequest(index);
  try {
    return client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}

@Test
public void test_get_index(){
    GetIndexResponse response = demo.getIndex(INDEX);
    Assert.assertEquals(INDEX, response.getIndices()[0]);
}
```

##### Get Mappings

```java
public GetMappingsResponse getMappings(String index) {
  GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
  getMappingsRequest.indices(index).includeDefaults();
  try {
    return client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}

@Test
public void test_get_index_mappings() {
    GetMappingsResponse response = demo.getMappings(INDEX);
    Map<String, MappingMetadata> mappings = response.mappings();
    Object properties = mappings.get(INDEX).sourceAsMap().get("properties");
    Assert.assertNotNull(properties);
}
```

##### Get Field Mappings

```java
public GetFieldMappingsResponse getFieldMappings(String index, String field) {
  GetFieldMappingsRequest request = new GetFieldMappingsRequest();
  request.indices(index);
  request.fields(field);
  try {
    return client.indices().getFieldMapping(request, RequestOptions.DEFAULT);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}

@Test
public void test_get_field_mappings() {
    GetFieldMappingsResponse response = demo.getFieldMappings(INDEX, "username");
    FieldMappingMetadata metadata = response.fieldMappings(INDEX, "username");
    Assert.assertEquals("username", metadata.fullName());
}
```

##### Update Mappings

```java
public AcknowledgedResponse updateMapping(String index, XContentBuilder builder) {
  PutMappingRequest request = new PutMappingRequest(index);
  request.source(builder);
  try {
    return client.indices().putMapping(request, RequestOptions.DEFAULT);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}

@Test
public void test_update_mapping(){
    Map<String, String> fieldTypeMap = new HashMap<>();
    fieldTypeMap.put("username", "text");
    fieldTypeMap.put("age", "long");
    fieldTypeMap.put("password", "text");
    fieldTypeMap.put("address", "text");
    fieldTypeMap.put("hobbies", "text");
    XContentBuilder xContentBuilder = demo.jsonContentBuilder(fieldTypeMap);
    AcknowledgedResponse response = demo.updateMapping(INDEX, xContentBuilder);
    Assert.assertTrue(response.isAcknowledged());
}
```

##### Get Settings

```java
public GetSettingsResponse getSettings(String index) {
  GetSettingsRequest request = new GetSettingsRequest();
  request.indices(index);
  try {
    return client.indices().getSettings(request, RequestOptions.DEFAULT);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}
@Test
public void test_get_settings() {
    GetSettingsResponse response = demo.getSettings(INDEX);
    assertEquals("1", response.getSetting(INDEX, "index.number_of_shards"));
}
```

##### Update Settings

```java
public AcknowledgedResponse updateSettings(String index, Map<String, Object> settings) {
    UpdateSettingsRequest request = new UpdateSettingsRequest();
    request.indices(index).settings(settings);
    try {
        return client.indices().putSettings(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
@Test
public void test_update_settings() {
  Map<String, Object> settings = new HashMap<>();
  settings.put("index.number_of_replicas", 1);
  AcknowledgedResponse response = demo.updateSettings(INDEX, settings);
  assertTrue(response.isAcknowledged());
}
```

##### Delete Index

```java
public AcknowledgedResponse deleteIndex(String index) {
  DeleteIndexRequest request = new DeleteIndexRequest();
  request.indices(index);
  try {
    return client.indices().delete(request, RequestOptions.DEFAULT);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}

@Test
public void test_delete_index() {
    AcknowledgedResponse response = demo.deleteIndex(INDEX);
    assertTrue(response.isAcknowledged());
}
```

##### Exists Index

```java
public boolean exists(String index) {
  GetIndexRequest request = new GetIndexRequest(index);
  try {
    return client.indices().exists(request, RequestOptions.DEFAULT);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}

@Test
public void test_index_exists() {
    if (demo.exists(INDEX)) {
        demo.deleteIndex(INDEX);
    }
    assertFalse(demo.exists(INDEX));
}
```

#### Document API

##### Create Document

```java
public IndexResponse insert(String index, Map<String, Object> source) {
  ThrowableFunction<IndexRequest, IndexResponse> function =
      (indexRequest) -> client.index(indexRequest, RequestOptions.DEFAULT);
  IndexRequest indexRequest =
      new IndexRequest()
          .index(index)
          .id(String.valueOf(idGenerator.next()))
          .source(source, XContentType.JSON)
          .opType(OpType.INDEX);
  return ThrowableFunction.execute(indexRequest, function);
}
@Test
public void test_create_document() {
    Map<String, Object> source = new HashMap<>();
    source.put("avaliable", true);
    source.put("date", new Date());
    source.put("price", new Random().nextLong());
    source.put("productID", UUID.randomUUID().toString());
    IndexResponse response = demo.insert(INDEX, source);
    Assert.assertEquals(1L,  response.getVersion());
}
```

注意：

```java
enum OpType {
    /**
     * Index the source. If there an existing document with the id, it will
     * be replaced.(default value)
     */
    INDEX(0),
    /**
     * Creates the resource. Simply adds it to the index, if there is an existing
     * document with the id, then it won't be removed.
     */
    CREATE(1),
    /** Updates a document */
    UPDATE(2),
    /** Deletes a document */
    DELETE(3);
}
```

##### Get Document

```java
 public GetResponse getDocument(String index, String id) {
    ThrowableFunction<GetRequest, GetResponse> function =
        getRequest -> client.get(getRequest, RequestOptions.DEFAULT);
    String[] includes = new String[] {"productID"};
    FetchSourceContext fetchSourceContext =
        new FetchSourceContext(true, includes, Strings.EMPTY_ARRAY);
    GetRequest getRequest =
        new GetRequest()
            .index(index)
            .id(id)
            .storedFields("date")
            .fetchSourceContext(fetchSourceContext);
    return ThrowableFunction.execute(getRequest, function);
  }

  @Test
  public void test_get_document(){
    GetResponse response = demo.getDocument(INDEX, "2");
    Map<String, Object> sourceAsMap = response.getSourceAsMap();
    Assert.assertTrue(sourceAsMap.containsKey("productID"));
    Assert.assertTrue(response.isExists());
  }
```

##### Get Source

```java
  public GetSourceResponse getDocumentSource(String index, String id) {
    ThrowableFunction<GetSourceRequest, GetSourceResponse> function =
        getSourceRequest -> client.getSource(getSourceRequest, RequestOptions.DEFAULT);
    GetSourceRequest getSourceRequest = new GetSourceRequest(index, id);
    return ThrowableFunction.execute(getSourceRequest, function);
  }

  @Test
  public void test_get_document_source(){
    GetSourceResponse documentSource = demo.getDocumentSource(INDEX, "2");
    Map<String, Object> source = documentSource.getSource();
    Assert.assertTrue(source.containsKey("productID"));
  }
```

##### Update Document

```java
public UpdateResponse updateDocument(String index, String id, Map<String, Object> params) {
  ThrowableFunction<UpdateRequest, UpdateResponse> function =
      updateRequest -> client.update(updateRequest, RequestOptions.DEFAULT);
  UpdateRequest updateRequest = new UpdateRequest(index, id).doc(params, XContentType.JSON);
  return ThrowableFunction.execute(updateRequest, function);
}

  @Test
  public void test_update_document() {
    Map<String, Object> params = new HashMap<>();
    params.put("price", 30);
    String id = "1";
    UpdateResponse updateResponse = demo.updateDocument(INDEX, id, params);
    Assert.assertEquals("1", updateResponse.getId());

    GetSourceResponse documentSource = demo.getDocumentSource(INDEX, id);
    Map<String, Object> source = documentSource.getSource();
    Assert.assertEquals("30", source.get("price").toString());
  }
```

##### Exists Document

```java
public boolean exists(String index, String id) {
  ThrowableFunction<GetRequest, Boolean> function =
    getRequest -> client.exists(getRequest, RequestOptions.DEFAULT);
  String[] includes = new String[] {"productID", "price", "date"};
  GetRequest getRequest =
    new GetRequest(index, id)
    .fetchSourceContext(new FetchSourceContext(true, includes, Strings.EMPTY_ARRAY));
  return ThrowableFunction.execute(getRequest, function);
}

@Test
public void test_exists(){
  boolean exists = demo.exists(INDEX, "1");
  assertTrue(exists);
}

@Test
public void test_not_exists(){
  boolean exists = demo.exists(INDEX, "100");
  assertFalse(exists);
}
```

##### Delete Document

```java
public DeleteResponse delete(String index, String id) {
  DeleteRequest deleteRequest = new DeleteRequest().index(index).id(id);
  return ThrowableFunction.execute(
      deleteRequest, request -> client.delete(request, RequestOptions.DEFAULT));
}

	@Test
  public void test_delete(){
    DeleteResponse response = demo.delete(INDEX, "1");
    RestStatus status = response.status();
    assertEquals(OK, status);
  }

  @Test
  public void test_delete_not_exists(){
    DeleteResponse response = demo.delete(INDEX, "100");
    RestStatus status = response.status();
    assertEquals(NOT_FOUND, status);
  }
```

##### Bulk

```java
public BulkResponse bulkAction(
    String index,
    Map<String, Map<String, Object>> indexRequests,
    Map<String, Map<String, Object>> updateRequests,
    List<String> deleteRequests) {
  BulkRequest bulkRequest = new BulkRequest();
  if (null != indexRequests && !indexRequests.isEmpty()) {
    indexRequests.forEach(
        (id, source) ->
            bulkRequest.add(
                new IndexRequest().index(index).id(id).source(source, XContentType.JSON)));
  }
  if (null != updateRequests && !updateRequests.isEmpty()) {
    updateRequests.forEach(
        (id, source) ->
            bulkRequest.add(
                new UpdateRequest().index(index).id(id).doc(source, XContentType.JSON)));
  }
  if (null != deleteRequests && !deleteRequests.isEmpty()) {
    deleteRequests.forEach(id -> bulkRequest.add(new DeleteRequest().index(index).id(id)));
  }
  return ThrowableFunction.execute(
      bulkRequest, request -> client.bulk(request, RequestOptions.DEFAULT));
}

  @Test
  public void test_bulk_api() throws InterruptedException {
    Map<String, Map<String, Object>> indexRequests = new HashMap<>();

    Map<String, Object> indexSource1 = new HashMap<>();
    indexSource1.put("avaliable", true);
    indexSource1.put("productID", UUID.randomUUID().toString());
    indexSource1.put("price", 33);
    indexSource1.put("date", new Date());
    TimeUnit.SECONDS.sleep(1);

    Map<String, Object> indexSource2 = new HashMap<>();
    indexSource2.put("avaliable", true);
    indexSource2.put("productID", UUID.randomUUID().toString());
    indexSource2.put("price", 44);
    indexSource2.put("date", new Date());
    TimeUnit.SECONDS.sleep(1);

    Map<String, Object> indexSource3 = new HashMap<>();
    indexSource3.put("avaliable", true);
    indexSource3.put("productID", UUID.randomUUID().toString());
    indexSource3.put("price", 55);
    indexSource3.put("date", new Date());

    indexRequests.put("11", indexSource1);
    indexRequests.put("12", indexSource2);
    indexRequests.put("13", indexSource3);

    Map<String, Map<String, Object>> updateRequests = new HashMap<>();
    Map<String, Object> updateSource1 = new HashMap<>();
    updateSource1.put("price", 66);

    Map<String, Object> updateSource2 = new HashMap<>();
    updateSource2.put("price", 77);
    updateRequests.put("11", updateSource1);
    updateRequests.put("13", updateSource2);

    List<String> deleteRequests = new ArrayList<>();
    deleteRequests.add("12");

    BulkResponse bulkResponse =
        demo.bulkAction(INDEX, indexRequests, updateRequests, deleteRequests);

    boolean hasFailures = bulkResponse.hasFailures();
    assertFalse(hasFailures);

    bulkResponse.forEach(
        response -> {
          DocWriteRequest.OpType opType = response.getOpType();
          switch (opType) {
            case INDEX:
            case CREATE:
              IndexResponse indexResponse = response.getResponse();
              DocWriteResponse.Result indexRequest = indexResponse.getResult();
              assertEquals(CREATED, indexRequest);
              break;
            case UPDATE:
              UpdateResponse updateResponse = response.getResponse();
              DocWriteResponse.Result updateResult = updateResponse.getResult();
              assertEquals(UPDATED, updateResult);
              break;
            case DELETE:
              DeleteResponse deleteResponse = response.getResponse();
              DocWriteResponse.Result deleteResult = deleteResponse.getResult();
              assertEquals(DELETED, deleteResult);
              break;
            default:
              throw new IllegalArgumentException(
                  String.format("No Supported opType [%s]", opType.name()));
          }
        });
  }
```

##### Multi-Get

```java
public MultiGetResponse multiGet(
    Map<String, List<String>> indexIdMap,
    Map<String, String[]> includesMap,
    Map<String, String[]> excludesMap) {
  MultiGetRequest multiGetRequest = new MultiGetRequest();
  indexIdMap.forEach(
      (index, idList) -> {
        String[] includes = includesMap.get(index);
        String[] excludes = excludesMap.get(index);
        idList.forEach(
            id ->
                multiGetRequest.add(
                    new MultiGetRequest.Item(index, id)
                        .fetchSourceContext(new FetchSourceContext(true, includes, excludes))));
      });
  return ThrowableFunction.execute(
      multiGetRequest, request -> client.mget(request, RequestOptions.DEFAULT));
}

 @Test
  public void test_multi_get() {
    Map<String, List<String>> indexIdsMap = new HashMap<>();
    List<String> productIdList = new ArrayList<>();
    productIdList.add("11");
    productIdList.add("13");
    indexIdsMap.put("products", productIdList);

    List<String> userIdList = new ArrayList<>();
    userIdList.add("2");
    userIdList.add("3");
    indexIdsMap.put("users", userIdList);

    Map<String, String[]> includesMap = new HashMap<>();
    includesMap.put("products", new String[] {"productID", "date", "price"});
    includesMap.put("users", new String[] {"username", "age", "address", "hobbies"});

    Map<String, String[]> excludesMap = new HashMap<>();
    excludesMap.put("products", Strings.EMPTY_ARRAY);
    excludesMap.put("users", Strings.EMPTY_ARRAY);

    MultiGetResponse multiGetResponse = demo.multiGet(indexIdsMap, includesMap, excludesMap);
    multiGetResponse.forEach(
        multiGetItemResponse -> {
          assertFalse(multiGetItemResponse.isFailed());
          GetResponse response = multiGetItemResponse.getResponse();
          assertTrue(response.isExists());
        });
  }
```

#### Search API

##### Search

```java
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
```



##### Multi-Search

```java
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
```

##### Search Template

```java
public SearchTemplateResponse searchTemplate() {
  return ThrowableFunction.execute(
      toSearchTemplateRequest("jackson"),
      request -> client.searchTemplate(request, RequestOptions.DEFAULT));
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

  @Test
  public void test_search_template() {
    SearchTemplateResponse searchTemplateResponse = demo.searchTemplate();
    for (SearchHit hit : searchTemplateResponse.getResponse().getHits()) {
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      sourceAsMap.forEach((key, value) -> log.info(key + " = " + value));
    }
  }

```

##### Multi-Search Template

```java
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
```

### Spring

### Spring boot

### Python REST Client

### Resource

[Java REST Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.9/java-rest-high.html)

[Python REST Client](https://www.elastic.co/guide/en/elasticsearch/client/python-api/7.9/index.html)

[Spring Data elasticsearch ](https://docs.spring.io/spring-data/elasticsearch/docs/4.2.2/reference/html/#elasticsearch.clients.rest)

