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

  public void close(RestHighLevelClient client) {
    if (client != null) {
      try {
        client.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void close() {
    close(client);
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

  public GetIndexResponse getIndex(String index) {
    GetIndexRequest getIndexRequest = new GetIndexRequest(index);
    try {
      return client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public GetMappingsResponse getMappings(String index) {
    GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
    getMappingsRequest.indices(index).includeDefaults();
    try {
      return client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

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

  public AcknowledgedResponse updateMapping(String index, XContentBuilder builder) {
    PutMappingRequest request = new PutMappingRequest(index);
    request.source(builder);
    try {
      return client.indices().putMapping(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public GetSettingsResponse getSettings(String index) {
    GetSettingsRequest request = new GetSettingsRequest();
    request.indices(index);
    try {
      return client.indices().getSettings(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public AcknowledgedResponse updateSettings(String index, Map<String, Object> settings) {
    UpdateSettingsRequest request = new UpdateSettingsRequest();
    request.indices(index).settings(settings);
    try {
      return client.indices().putSettings(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public AcknowledgedResponse deleteIndex(String index) {
    DeleteIndexRequest request = new DeleteIndexRequest();
    request.indices(index);
    try {
      return client.indices().delete(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean exists(String index) {
    GetIndexRequest request = new GetIndexRequest(index);
    try {
      return client.indices().exists(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
