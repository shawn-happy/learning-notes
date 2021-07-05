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

##### Get Source

##### Update Document

##### Exists Document

##### Delete Document

##### Bulk

##### Multi-Get

#### Search API

##### Search

##### Multi-Search

##### Search Template

##### Multi-Search Template

### Spring

### Spring boot

### Python REST Client

### Resource

[Java REST Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.9/java-rest-high.html)

[Python REST Client](https://www.elastic.co/guide/en/elasticsearch/client/python-api/7.9/index.html)

[Spring Data elasticsearch ](https://docs.spring.io/spring-data/elasticsearch/docs/4.2.2/reference/html/#elasticsearch.clients.rest)

