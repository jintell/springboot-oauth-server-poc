package com.jade.platform.web;

import com.jade.platform.model.AccessToken;
import com.jade.platform.service.AuthTokenService;
import com.jade.platform.service.ResourceOwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 9/19/23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiResource {

    private final AuthTokenService tokenService;
    private final ResourceOwnerService resourceOwnerService;
    public static final String ROLE_ADMIN = "hasAuthority('ADMIN')";

    @GetMapping("/request/authorize/url/{deviceId}")
    public ResponseEntity<Mono<String>> getAuthorizedUrl(@PathVariable String deviceId) {
        log.info("{}", deviceId);
        return ResponseEntity.ok(resourceOwnerService.requestAuthorizedUrl(deviceId));
    }


    @GetMapping("/info")
    public Mono<ResponseEntity<String>> getInfo() {
        return Mono.just(ResponseEntity.ok("This is a public welcome info"));
    }

    @PostMapping("/auth/code/endpoint")
    public ResponseEntity<Mono<AccessToken>> getAccessToken(@RequestParam String code,
                                                            @RequestParam String deviceId) {
        log.trace("{}\n{}", code, deviceId);
        return ResponseEntity.ok(tokenService.exchangeWithAccessToken(code, deviceId));
    }

    @GetMapping("/auth/code/logout")
    public ResponseEntity<Mono<String>> logout() {
        return ResponseEntity.ok(resourceOwnerService.requestLogoutEndpoint());
    }

    @GetMapping("/hello")
    public Mono<ResponseEntity<String>> getGreetings() {
        return Mono.just(ResponseEntity.ok("Hello User"));
    }

    @PreAuthorize(ROLE_ADMIN)
    @GetMapping("/hello-admin")
    public Mono<ResponseEntity<String>> getAdminGreetings() {
        return Mono.just(ResponseEntity.ok("Hello Admin User"));
    }
}
