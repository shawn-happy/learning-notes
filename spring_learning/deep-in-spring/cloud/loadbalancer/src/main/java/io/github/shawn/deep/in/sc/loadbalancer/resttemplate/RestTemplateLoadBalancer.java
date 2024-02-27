package io.github.shawn.deep.in.sc.loadbalancer.resttemplate;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validator;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RestTemplateLoadBalancer {

  @Bean
  public ClientHttpRequestFactory okHttp3ClientHttpRequestFactory(){
    return new OkHttp3ClientHttpRequestFactory();
  }

  @Bean
  public HttpMessageConverter<?> mappingJackson2HttpMessageConverter(){
    return new MappingJackson2HttpMessageConverter();
  }

  @Bean
  public ClientHttpRequestInterceptor clientHttpRequestInterceptor(Validator validator) {
    return new ValidationClientHttpRequestInterceptor(
        validator, mappingJackson2HttpMessageConverter());
  }


  @Bean
  public RestTemplate restTemplate(List<ClientHttpRequestInterceptor> interceptors) {
    AnnotationAwareOrderComparator.sort(interceptors);
    return new RestTemplateBuilder()
            .messageConverters(mappingJackson2HttpMessageConverter())
            .interceptors(interceptors)
            .requestFactory(this::okHttp3ClientHttpRequestFactory).build();
  }
}
