package org.meldtech.platform.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Map;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final String apiKey;

    public ApiKeyAuthenticationToken(String apiKey, boolean authenticated) {
        super(authenticated ? AuthorityUtils.createAuthorityList("ROLE_API") : AuthorityUtils.NO_AUTHORITIES);
        this.apiKey = apiKey;
        setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return "API_KEY_USER";
    }

    public Map<String, Object> getDetails() {
        return Map.of("error", "invalid_api_key", "message", "User not authenticated.");
    }
}
