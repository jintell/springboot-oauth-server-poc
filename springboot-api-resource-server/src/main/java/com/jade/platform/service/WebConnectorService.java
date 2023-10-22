package com.jade.platform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;


/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 9/21/23
 */
@Slf4j
@Component
public class WebConnectorService {
    private final WebClient webClient;
    public WebConnectorService() {
        this.webClient = customWebClient();
    }

    public <T> Mono<T> postForm(String endpoint,
                                MultiValueMap<String, String> requestBody,
                                Map<String, String> headers,
                                Class<T> returnType) {
        log.info("{}", endpoint);
        return webClient.post()
                .uri(endpoint)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set)  )
                .body(BodyInserters.fromFormData( requestBody))
                .retrieve()
                .bodyToMono(returnType);
    }



    private WebClient customWebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .wiretap(true)
                        .followRedirect(true)))
                .defaultHeader("Accept", APPLICATION_JSON)
                .defaultHeader("Content-Type", APPLICATION_JSON)
                .build();
    }

}
