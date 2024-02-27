package io.github.shawn.deep.in.sc.loadbalancer.webClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/lb/webclient")
public class WebClientController {

    private final WebClient.Builder builder;

//    @Autowired
//    private ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public WebClientController(WebClient.Builder builder) {
        this.builder = builder;
    }

    @GetMapping("/getValue")
    public String getValue() {
        String block = builder
                .baseUrl("http://nacos-config")
                .build()
                .get().uri("/nacos/config").retrieve()
                .bodyToMono(String.class).onErrorComplete(e -> {
                    e.printStackTrace();
                    return false;
                }).block();
        return block;
    }
}
