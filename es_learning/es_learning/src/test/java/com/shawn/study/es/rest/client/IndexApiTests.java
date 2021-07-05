package com.shawn.study.es.rest.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetFieldMappingsResponse;
import org.elasticsearch.client.indices.GetFieldMappingsResponse.FieldMappingMetadata;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IndexApiTests {

  private static final String HOST = "localhost";
  private static final int PORT = 9200;
  private static final String SCHEMA = "http";
  private static final String INDEX = "java_rest_client_demo";
  private IndexApiDemo demo;

  @Before
  public void init() {
    demo = new IndexApiDemo(HOST, PORT, SCHEMA);
    if (demo.exists(INDEX)) {
      demo.deleteIndex(INDEX);
    }
    create_index(INDEX);
  }

  @After
  public void destroy() {
    demo.close();
  }

  private CreateIndexResponse create_index(String index) {
    Settings settings =
        demo.settings(
            Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 0));
    Map<String, String> fieldTypeMap = new HashMap<>();
    fieldTypeMap.put("username", "text");
    fieldTypeMap.put("age", "long");
    fieldTypeMap.put("password", "text");
    fieldTypeMap.put("address", "text");
    XContentBuilder xContentBuilder = demo.jsonContentBuilder(fieldTypeMap);
    return demo.createIndex(index, settings, xContentBuilder);
  }

  @Test
  public void test_create_index() {
    if (demo.exists("java_client_test")) {
      demo.deleteIndex("java_client_test");
    }
    assertTrue(create_index("java_client_test").isAcknowledged());
    demo.deleteIndex("java_client_test");
  }

  @Test
  public void test_get_index() {
    GetIndexResponse response = demo.getIndex(INDEX);
    assertEquals(INDEX, response.getIndices()[0]);
  }

  @Test
  public void test_get_index_mappings() {
    GetMappingsResponse response = demo.getMappings(INDEX);
    Map<String, MappingMetadata> mappings = response.mappings();
    Object properties = mappings.get(INDEX).sourceAsMap().get("properties");
    assertNotNull(properties);
  }

  @Test
  public void test_get_field_mappings() {
    GetFieldMappingsResponse response = demo.getFieldMappings(INDEX, "username");
    FieldMappingMetadata metadata = response.fieldMappings(INDEX, "username");
    assertEquals("username", metadata.fullName());
  }

  @Test
  public void test_update_mapping() {
    Map<String, String> fieldTypeMap = new HashMap<>();
    fieldTypeMap.put("username", "text");
    fieldTypeMap.put("age", "long");
    fieldTypeMap.put("password", "text");
    fieldTypeMap.put("address", "text");
    fieldTypeMap.put("hobbies", "text");
    XContentBuilder xContentBuilder = demo.jsonContentBuilder(fieldTypeMap);
    AcknowledgedResponse response = demo.updateMapping(INDEX, xContentBuilder);
    assertTrue(response.isAcknowledged());
  }

  @Test
  public void test_get_settings() {
    GetSettingsResponse response = demo.getSettings(INDEX);
    assertEquals("1", response.getSetting(INDEX, "index.number_of_shards"));
  }

  @Test
  public void test_update_settings() {
    Map<String, Object> settings = new HashMap<>();
    settings.put("index.number_of_replicas", 1);
    AcknowledgedResponse response = demo.updateSettings(INDEX, settings);
    assertTrue(response.isAcknowledged());
  }

  @Test
  public void test_delete_index() {
    AcknowledgedResponse response = demo.deleteIndex(INDEX);
    assertTrue(response.isAcknowledged());
  }

  @Test
  public void test_index_exists() {
    if (demo.exists(INDEX)) {
      demo.deleteIndex(INDEX);
    }
    assertFalse(demo.exists(INDEX));
  }
}
