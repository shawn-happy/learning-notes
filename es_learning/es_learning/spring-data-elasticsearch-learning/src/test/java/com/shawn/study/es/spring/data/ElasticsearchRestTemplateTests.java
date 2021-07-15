package com.shawn.study.es.spring.data;

import com.shawn.study.es.spring.data.service.ElasticsearchTemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchRestTemplateTests {

  @Autowired private ElasticsearchTemplateService demo;

  @Test
  public void test_create_index() throws Exception {
    Map<String, Object> settings = new HashMap<>();
    settings.put("index.number_of_shards", 1);
    settings.put("index.number_of_replicas", 0);
    Document mapping = Document.create();
    String json =
        "{\n"
            + "  \"properties\": {\n"
            + "    \"id\": {\n"
            + "      \"type\": \"text\"\n"
            + "    },\n"
            + "    \"name\": {\n"
            + "      \"type\": \"text\"\n"
            + "    },\n"
            + "    \"price\": {\n"
            + "      \"type\": \"long\"\n"
            + "    }\n"
            + "  }\n"
            + "}";
    mapping.fromJson(json);
    assertTrue(demo.createIndex(settings, mapping));
    assertTrue(demo.existsIndex());
    assertEquals("1", demo.getSettings().get("index.number_of_shards").toString());
    assertTrue(demo.getMappings().containsKey("properties"));
    mapping = Document.create();
    json =
        "{\n"
            + "  \"properties\": {\n"
            + "    \"id\": {\n"
            + "      \"type\": \"text\"\n"
            + "    },\n"
            + "    \"name\": {\n"
            + "      \"type\": \"text\"\n"
            + "    },\n"
            + "    \"price\": {\n"
            + "      \"type\": \"long\"\n"
            + "    },\n"
            + "    \"description\": {\n"
            + "      \"type\": \"text\"\n"
            + "    }\n"
            + "  }\n"
            + "}";
    mapping.fromJson(json);
    assertTrue(demo.updateIndex(mapping));
    assertTrue(demo.deleteIndex());
    assertFalse(demo.existsIndex());
  }
}
