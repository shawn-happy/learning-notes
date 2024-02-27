package io.github.shawn.deep.in.sc.loadbalancer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "nacos-config", path = "/nacos")
public interface FeignClientService {

    @GetMapping("/config")
    String getValue();

}
