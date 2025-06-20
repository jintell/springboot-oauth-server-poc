package org.meldtech.platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    private final ApiKeyAuthFilter apiKeyAuthFilter;

    public SecurityConfig(ApiKeyAuthFilter apiKeyAuthFilter) {
        this.apiKeyAuthFilter = apiKeyAuthFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .securityContextRepository(apiKeyAuthFilter)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/public/**").permitAll()
                        .pathMatchers("/webjars/**").permitAll()
                        .pathMatchers("/swagger-ui/**").permitAll()
                        .pathMatchers("/swagger-ui**").permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }
}
