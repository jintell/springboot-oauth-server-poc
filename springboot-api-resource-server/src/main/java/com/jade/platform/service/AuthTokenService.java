package com.jade.platform.service;

import com.jade.platform.constant.AppUtil;
import com.jade.platform.encoding.MessageEncoding;
import com.jade.platform.model.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 9/22/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final WebConnectorService webConnectorService;

    @Value("${oauth2.client.id}")
    private String clientId;
    @Value("${oauth2.client.secret}")
    private String clientSecret;
    @Value("${oauth2.authorization.url}")
    private String authorizationUrl;
    @Value("${oauth2.authorization.grant_type}")
    private String grantType;
    @Value("${oauth2.authorization.redirect_uri}")
    private String redirectUrl;

    public Mono<AccessToken> exchangeWithAccessToken(String code, String deviceId) {
        String codeVerifier = AppUtil.USER_DEVICE.get(deviceId);
        var url = buildUrl(code, codeVerifier);
        log.trace("Fetching auth token with: {}", url);
        return webConnectorService.postForm(url, new LinkedMultiValueMap<>(), headers(), AccessToken.class);
    }

    private String buildUrl(String code, String codeVerifier) {
        return String.format("%s?client_id=%s&grant_type=%s&redirect_uri=%s&code=%s&code_verifier=%s",
                authorizationUrl,
                clientId,
                grantType,
                redirectUrl,
                code,
                codeVerifier);
    }

    private Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content_Type", MediaType.APPLICATION_JSON_VALUE);
        headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.put("Authorization", "Basic " +
                MessageEncoding.base64Encoding(String.format("%s:%s", clientId, clientSecret)) );
        return headers;
    }
}
