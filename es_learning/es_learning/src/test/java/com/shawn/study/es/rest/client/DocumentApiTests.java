package com.shawn.study.es.rest.client;

import static org.elasticsearch.action.DocWriteResponse.Result.*;
import static org.elasticsearch.rest.RestStatus.NOT_FOUND;
import static org.elasticsearch.rest.RestStatus.OK;
import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.junit.After;
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
    IndexResponse response = demo.insert(id, INDEX, source);
    assertEquals(1L, response.getVersion());
  }

  @Test
  public void test_get_document() {
    GetResponse response = demo.getDocument(INDEX, "2");
    Map<String, Object> sourceAsMap = response.getSourceAsMap();
    assertTrue(sourceAsMap.containsKey("productID"));
    assertTrue(response.isExists());
  }

  @Test
  public void test_get_document_source() {
    GetSourceResponse documentSource = demo.getDocumentSource(INDEX, "2");
    Map<String, Object> source = documentSource.getSource();
    assertTrue(source.containsKey("productID"));
  }

  @Test
  public void test_update_document() {
    Map<String, Object> params = new HashMap<>();
    params.put("price", 10);
    String id = "1";
    UpdateResponse updateResponse = demo.updateDocument(INDEX, id, params);
    assertEquals("1", updateResponse.getId());
    assertEquals(UPDATED, updateResponse.getResult());

    GetResult getResult = updateResponse.getGetResult();
    if (null != getResult && getResult.isExists()) {
      Map<String, Object> map = getResult.sourceAsMap();
      assertEquals("10", map.get("price").toString());
    }
  }

  @Test
  public void test_exists() {
    boolean exists = demo.exists(INDEX, "1");
    assertTrue(exists);
  }

  @Test
  public void test_not_exists() {
    boolean exists = demo.exists(INDEX, "100");
    assertFalse(exists);
  }

  @Test
  public void test_delete() {
    DeleteResponse response = demo.delete(INDEX, "1");
    RestStatus status = response.status();
    assertEquals(OK, status);
  }

  @Test
  public void test_delete_not_exists() {
    DeleteResponse response = demo.delete(INDEX, "100");
    RestStatus status = response.status();
    assertEquals(NOT_FOUND, status);
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
}
