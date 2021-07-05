package com.shawn.study.es.rest.client;

import java.util.Map;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest.OpType;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * java rest client - Document Basic CURD APIs
 *
 * @author Shawn
 * @since 1.0
 */
public class DocumentApiDemo {

  private final RestHighLevelClient client;

  private static int id = 0;

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
    GetRequest getRequest = new GetRequest().index(index).id(id).storedFields("date");
    getRequest.fetchSourceContext();
    return ThrowableFunction.execute(getRequest, function);
  }
}
