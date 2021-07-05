package com.shawn.study.es.rest.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentApiTests {

  private static final String HOST = "172.27.67.41";
  private static final int PORT = 9200;
  private static final String SCHEMA = "http";
  private static final String INDEX = "products";

  private static final String id = "1";
  private DocumentApiDemo demo;


  @Before
  public void init() {
    demo = new DocumentApiDemo(HOST, PORT, SCHEMA);
  }

  @After
  public void destroy() {
    demo.close();
  }

  @Test
  public void test_create_document() {
    Map<String, Object> source = new HashMap<>();
    source.put("avaliable", true);
    source.put("date", new Date());
    source.put("price", new Random().nextLong());
    source.put("productID", UUID.randomUUID().toString());
    IndexResponse response = demo.insert(id,INDEX, source);
    Assert.assertEquals(1L,  response.getVersion());
  }

  @Test
  public void test_get_document(){
    GetResponse response = demo.getDocument(INDEX, "2");
    Assert.assertTrue(response.isExists());
  }
}
