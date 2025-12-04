package io.github.shawn.deep.in.llm.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.Map;
import org.springframework.ai.mcp.client.autoconfigure.properties.McpClientCommonProperties.Toolcallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class WebFluxMcpServerDemo {

  public static void main(String[] args){
    SpringApplication.run(WebFluxMcpServerDemo.class, args);
  }

  @Bean
  public ToolCallbackProvider weatherTools(OpenMeteoService openMeteoService) {
    return MethodToolCallbackProvider.builder().toolObjects(openMeteoService).build();
  }

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

//  @Bean
//  public List<SyncResourceSpecification> myResources() {
//    var systemInfoResource = new McpSchema.Resource(...);
//    var resourceSpecification = new McpServerFeatures.SyncResourceSpecification(systemInfoResource, (exchange, request) -> {
//      try {
//        var systemInfo = Map.of(...);
//        String jsonContent = new ObjectMapper().writeValueAsString(systemInfo);
//        return new McpSchema.ReadResourceResult(
//            List.of(new McpSchema.TextResourceContents(request.uri(), "application/json", jsonContent)));
//      }
//      catch (Exception e) {
//        throw new RuntimeException("Failed to generate system info", e);
//      }
//    });
//
//    return List.of(resourceSpecification);
//  }
}
