package io.github.shawn.deep.in.sc.loadbalancer.resttemplate;

import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import javax.validation.Validator;
import java.io.IOException;

public class ValidationClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

    private final Validator validator;

    private final HttpMessageConverter<?>[] converters;

    public ValidationClientHttpRequestInterceptor(Validator validator, HttpMessageConverter<?>... converters) {
        this.validator = validator;
        this.converters = converters;
    }
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        ClientHttpResponse response = null;
        // 前置处理
        beforeExecute(request, body);

        response = execution.execute(request, body);
        // 后置处理
        return afterExecute(response);
    }

    private void beforeExecute(HttpRequest request, byte[] body) {

    }

    private ClientHttpResponse afterExecute(ClientHttpResponse response){
        return response;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
