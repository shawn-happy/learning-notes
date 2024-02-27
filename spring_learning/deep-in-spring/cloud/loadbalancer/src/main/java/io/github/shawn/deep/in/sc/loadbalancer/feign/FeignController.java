package io.github.shawn.deep.in.sc.loadbalancer.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lb/feign")
public class FeignController {
    private final FeignClientService feignClientService;

    public FeignController(FeignClientService feignClientService) {
        this.feignClientService = feignClientService;
    }

    @GetMapping("/getValue")
    public String getValue(){
        return feignClientService.getValue();
    }

}
