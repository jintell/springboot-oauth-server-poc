package com.jae.platform.service;

import com.jae.platform.model.AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 9/21/23
 */
@Service
@RequiredArgsConstructor
public class GreetingAppService {
    @Value("${clientApp.server.url}")
    private String clientAppUrl;

    private final WebConnectorService webConnectorService;

    public Mono<String> getGreetings(AccessToken accessToken) {
        return webConnectorService.get(clientAppUrl, headers(accessToken.getToken()), String.class);
    }

    private Map<String, String> headers(String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
