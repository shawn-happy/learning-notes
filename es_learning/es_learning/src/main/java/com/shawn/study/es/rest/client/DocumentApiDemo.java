package com.shawn.study.es.rest.client;

import java.util.List;
import java.util.Map;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest.OpType;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

/**
 * java rest client - Document Basic CURD APIs
 *
 * @author Shawn
 * @since 1.0
 */
public class DocumentApiDemo {

  private final RestHighLevelClient client;

  public DocumentApiDemo(String host, int port, String schema) {
    client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, schema)));
  }

  public void close() {
    ThrowableAction.execute(client::close);
  }

  public IndexResponse insert(String id, String index, Map<String, Object> source) {
    ThrowableFunction<IndexRequest, IndexResponse> function =
        (indexRequest) -> client.index(indexRequest, RequestOptions.DEFAULT);
    IndexRequest indexRequest =
        new IndexRequest()
            .index(index)
            .id(id)
            .source(source, XContentType.JSON)
            .opType(OpType.INDEX);
    return ThrowableFunction.execute(indexRequest, function);
  }

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

  public GetSourceResponse getDocumentSource(String index, String id) {
    ThrowableFunction<GetSourceRequest, GetSourceResponse> function =
        getSourceRequest -> client.getSource(getSourceRequest, RequestOptions.DEFAULT);
    GetSourceRequest getSourceRequest = new GetSourceRequest(index, id);
    return ThrowableFunction.execute(getSourceRequest, function);
  }

  public UpdateResponse updateDocument(String index, String id, Map<String, Object> params) {
    ThrowableFunction<UpdateRequest, UpdateResponse> function =
        updateRequest -> client.update(updateRequest, RequestOptions.DEFAULT);
    UpdateRequest updateRequest = new UpdateRequest(index, id).doc(params, XContentType.JSON);
    String[] includes = new String[] {"productID", "price", "date"};
    FetchSourceContext fetchSourceContext =
        new FetchSourceContext(true, includes, Strings.EMPTY_ARRAY);
    updateRequest.fetchSource(fetchSourceContext);
    return ThrowableFunction.execute(updateRequest, function);
  }

  public boolean exists(String index, String id) {
    ThrowableFunction<GetRequest, Boolean> function =
        getRequest -> client.exists(getRequest, RequestOptions.DEFAULT);
    String[] includes = new String[] {"productID", "price", "date"};
    GetRequest getRequest =
        new GetRequest(index, id)
            .fetchSourceContext(new FetchSourceContext(true, includes, Strings.EMPTY_ARRAY));
    return ThrowableFunction.execute(getRequest, function);
  }

  public DeleteResponse delete(String index, String id) {
    DeleteRequest deleteRequest = new DeleteRequest().index(index).id(id);
    return ThrowableFunction.execute(
        deleteRequest, request -> client.delete(request, RequestOptions.DEFAULT));
  }

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
}
