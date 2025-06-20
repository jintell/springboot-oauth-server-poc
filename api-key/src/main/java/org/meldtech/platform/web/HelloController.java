package org.meldtech.platform.web;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@SecurityRequirement(name = "ApiKeyAuth")
public class HelloController {
    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello, your API key is valid!");
    }

    @GetMapping("/public/ping")
    public Mono<String> ping() {
        return Mono.just("No key needed here!");
    }
}
