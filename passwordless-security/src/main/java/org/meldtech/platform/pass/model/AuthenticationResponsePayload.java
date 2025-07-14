package org.meldtech.platform.pass.model;

import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;

public record AuthenticationResponsePayload(String username,
                                            PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential
) { }
