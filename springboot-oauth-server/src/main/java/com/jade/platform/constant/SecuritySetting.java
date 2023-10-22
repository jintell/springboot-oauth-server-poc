package com.jade.platform.constant;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 9/19/23
 */
public interface SecuritySetting {
    ClientAuthenticationMethod CLIENT_AUTHENTICATION_METHOD_BASIC = ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
    ClientAuthenticationMethod CLIENT_AUTHENTICATION_METHOD_NONE = ClientAuthenticationMethod.NONE;
    AuthorizationGrantType AUTHORIZATION_GRANT_TYPE_CODE = AuthorizationGrantType.AUTHORIZATION_CODE;
    AuthorizationGrantType AUTHORIZATION_DEVICE_CODE = AuthorizationGrantType.DEVICE_CODE;
    AuthorizationGrantType AUTHORIZATION_GRANT_TYPE_REFRESH_TOKEN = AuthorizationGrantType.REFRESH_TOKEN;
    AuthorizationGrantType AUTHORIZATION_GRANT_TYPE_CLIENT_CREDENTIALS = AuthorizationGrantType.CLIENT_CREDENTIALS;

    interface Scope {
        String OIDC_OPEN_ID = OidcScopes.OPENID;
        String OIDC_EMAIL = OidcScopes.EMAIL;
        String OIDC_PROFILE = OidcScopes.PROFILE;
        String READ_WRITE = "READ_WRITE";
        String ADMIN_PERMISSION = "ADMIN";
    }
}
