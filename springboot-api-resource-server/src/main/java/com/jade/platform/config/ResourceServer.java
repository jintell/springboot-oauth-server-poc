package com.jade.platform.config;

import com.jade.platform.config.helper.CombinedClaimConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 *  @Author: Josiah Adetayo
 *  @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 *  @Date: 9/19/23
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class ResourceServer {

    private final CombinedClaimConverter converter;
    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers(AUTH_WHITELIST)
                                .permitAll()
                                .anyExchange()
                                .authenticated()
                )
                .oauth2ResourceServer(oAuth2 -> oAuth2.jwt(jwtSpec ->
                        jwtSpec.jwtAuthenticationConverter(converter)));
        http.csrf(ServerHttpSecurity.CsrfSpec::disable); // The CSRF should be configured for the production code
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOrigin("http://127.0.0.1:3000,http://localhost:3000,http://127.0.0.1:8080");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    private static final String[] AUTH_WHITELIST = {
            "/info",
            "/auth/code/**",
            "/request/authorize/**",
            // other public endpoints of your API may be appended to this array
    };
}
