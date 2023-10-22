package com.jae.platform.service;

import com.jae.platform.model.AccessToken;
import com.jae.platform.util.MessageEncoding;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 9/21/23
 */
@Component
@RequiredArgsConstructor
public class TokenService {
    @Value("${authorization.server.url}")
    private String authServerUrl;
    @Value("${authorization.server.clientId}")
    private String clientId;
    @Value("${authorization.server.clientSecret}")
    private String secret;
    @Value("${authorization.server.scope}")
    private String scope;
    @Value("${authorization.grantType}")
    private String grantType;

    private final WebConnectorService webConnectorService;

    public Mono<AccessToken> getAccessToken() {
        try {
            return webConnectorService.postForm(authServerUrl, formData(), headers(), AccessToken.class);
        }catch (Exception ex) {
            return Mono.empty();
        }
    }

    private MultiValueMap<String, String> formData() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.put("grant_type", Collections.singletonList(grantType));
        form.put("scope", Collections.singletonList(scope));
        return form;
    }


    private Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content_Type", MediaType.APPLICATION_JSON_VALUE);
        headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.put("Authorization", "Basic " +
                MessageEncoding.base64Encoding(String.format("%s:%s", clientId, secret)) );
        return headers;
    }




}
