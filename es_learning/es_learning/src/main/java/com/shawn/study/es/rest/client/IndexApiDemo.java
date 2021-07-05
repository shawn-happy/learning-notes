package com.shawn.study.es.rest.client;

import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetFieldMappingsRequest;
import org.elasticsearch.client.indices.GetFieldMappingsResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class IndexApiDemo {

  private final RestHighLevelClient client;

  public IndexApiDemo(String host, int port, String schema) {
    client = connect(host, port, schema);
  }

  public RestHighLevelClient connect(String host, int port, String schema) {
    return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, schema)));
  }

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
                ThrowableAction.execute(
                    () -> {
                      xContentBuilder.startObject(key);
                      {
                        xContentBuilder.field("type", value);
                      }
                      xContentBuilder.endObject();
                    });
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

  public void close(RestHighLevelClient client) {
    ThrowableAction.execute(client::close);
  }

  public void close() {
    close(client);
  }

  public CreateIndexResponse createIndex(
      String index, Settings settings, XContentBuilder xContentBuilder) {
    ThrowableFunction<CreateIndexRequest, CreateIndexResponse> function =
        createIndexRequest -> client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
    createIndexRequest.settings(settings);
    createIndexRequest.mapping(xContentBuilder);
    return ThrowableFunction.execute(createIndexRequest, function);
  }

  public GetIndexResponse getIndex(String index) {
    ThrowableFunction<GetIndexRequest, GetIndexResponse> function =
        getIndexRequest -> client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
    GetIndexRequest getIndexRequest = new GetIndexRequest(index);
    return ThrowableFunction.execute(getIndexRequest, function);
  }

  public GetMappingsResponse getMappings(String index) {
    ThrowableFunction<GetMappingsRequest, GetMappingsResponse> function =
        getMappingsRequest ->
            client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
    GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
    getMappingsRequest.indices(index).includeDefaults();
    return ThrowableFunction.execute(getMappingsRequest, function);
  }

  public GetFieldMappingsResponse getFieldMappings(String index, String field) {
    ThrowableFunction<GetFieldMappingsRequest, GetFieldMappingsResponse> function =
        getFieldMappingsRequest ->
            client.indices().getFieldMapping(getFieldMappingsRequest, RequestOptions.DEFAULT);
    GetFieldMappingsRequest request = new GetFieldMappingsRequest();
    request.indices(index);
    request.fields(field);
    return ThrowableFunction.execute(request, function);
  }

  public AcknowledgedResponse updateMapping(String index, XContentBuilder builder) {
    ThrowableFunction<PutMappingRequest, AcknowledgedResponse> function =
        putMappingRequest -> client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
    PutMappingRequest request = new PutMappingRequest(index);
    request.source(builder);
    return ThrowableFunction.execute(request, function);
  }

  public GetSettingsResponse getSettings(String index) {
    ThrowableFunction<GetSettingsRequest, GetSettingsResponse> function =
        getSettingsRequest ->
            client.indices().getSettings(getSettingsRequest, RequestOptions.DEFAULT);
    GetSettingsRequest request = new GetSettingsRequest();
    request.indices(index);
    return ThrowableFunction.execute(request, function);
  }

  public AcknowledgedResponse updateSettings(String index, Map<String, Object> settings) {
    ThrowableFunction<UpdateSettingsRequest, AcknowledgedResponse> function =
        updateSettingsRequest ->
            client.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);
    UpdateSettingsRequest request = new UpdateSettingsRequest();
    request.indices(index).settings(settings);
    return ThrowableFunction.execute(request, function);
  }

  public AcknowledgedResponse deleteIndex(String index) {
    ThrowableFunction<DeleteIndexRequest, AcknowledgedResponse> function =
        deleteIndexRequest -> client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    DeleteIndexRequest request = new DeleteIndexRequest();
    request.indices(index);
    return ThrowableFunction.execute(request, function);
  }

  public boolean exists(String index) {
    ThrowableFunction<GetIndexRequest, Boolean> function =
        getIndexRequest -> client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    GetIndexRequest request = new GetIndexRequest(index);
    return ThrowableFunction.execute(request, function);
  }
}
