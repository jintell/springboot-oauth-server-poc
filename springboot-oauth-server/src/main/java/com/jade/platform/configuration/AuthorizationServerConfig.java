package com.jade.platform.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static com.jade.platform.constant.SecuritySetting.*;
import static com.jade.platform.constant.SecuritySetting.Scope.*;
import static com.jade.platform.jwk.Jwks.generateRsa;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 9/19/23
 */
@Slf4j
@Configuration
public class AuthorizationServerConfig {

    @Value("${issuer.uri}")
    private String issuerUri;

    private RSAKey genRsaKey;

    // Configure with some Defaults the OAuth2 Authorization Server Configurer
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0
        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("http://localhost:3000/sign-in")
                        )
                );
        return http.build();
    }

    /**
     * An instance of RegisteredClientRepository is used to
     * both configure and managing clients
     * @return RegisteredClientRepository
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        var spaClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("client_spa")
                .clientSecret("{noop}spa_secret")
                .clientAuthenticationMethod(CLIENT_AUTHENTICATION_METHOD_BASIC)
                .authorizationGrantType(AUTHORIZATION_GRANT_TYPE_CODE)
                .authorizationGrantType(AUTHORIZATION_GRANT_TYPE_REFRESH_TOKEN)
                .scope(OIDC_OPEN_ID)
                .scope(OIDC_PROFILE)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(true) // this method enforces the use of PKCE for a particular client
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .authorizationCodeTimeToLive(Duration.ofSeconds(10))
                        .build())
                .redirectUri("http://127.0.0.1:3000/process/auth/login")
                .build();

        var clientServerApp = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("client_serverapp")
                .clientSecret("{noop}serverapp_secret")
                .clientAuthenticationMethod(CLIENT_AUTHENTICATION_METHOD_BASIC)
                .authorizationGrantType(AUTHORIZATION_GRANT_TYPE_CLIENT_CREDENTIALS)
                .scope(ADMIN_PERMISSION)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .jwkSetUrl(issuerUri)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .build())
                .build();

        var clientMobile = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("client_mobile")
                .clientSecret("{noop}mobile_secret")
                .clientAuthenticationMethod(CLIENT_AUTHENTICATION_METHOD_NONE)
                .authorizationGrantType(AUTHORIZATION_DEVICE_CODE)
                .scope(OIDC_PROFILE)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(spaClient, clientServerApp, clientMobile);
    }

    /**
     * A UserDetailsService is used for retrieving users to authenticate.
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService users() {
        var user = User.builder()
                .username("simpleuser")
                .password("{noop}12345")
                .roles("USER")
                .build();
        var admin = User.builder()
                .username("adminuser")
                .password("{noop}12345")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * A jwtCustomizer configure what is needed to be added to the JWT token.
     * @return OAuth2TokenCustomizer<JwtEncodingContext>
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            JwtClaimsSet.Builder claims = context.getClaims();
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                // Customize roles claims for access_token for authorization grant types
                if(context.getAuthorizationGrantType() == AuthorizationGrantType.AUTHORIZATION_CODE) {
                    List<String> roles = context.getPrincipal().getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList();
                    claims.claim("roles", roles).build();
                }
            }
            else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                // Customize headers/claims for id_token
                log.error("No Implementation for OPEN ID Connect yet");
            }
        };
    }

    @Bean
    public JWKSet jwkSet() {
        return new JWKSet(generateRsaKey());
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        genRsaKey = rsaKey;
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private RSAKey generateRsaKey() {
        if(genRsaKey == null) genRsaKey = generateRsa();
        return genRsaKey;
    }

    /**
     * Configure Spring Authorization Server issuer
     * @return AuthorizationServerSettings
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(issuerUri)
                .build();
    }

}
