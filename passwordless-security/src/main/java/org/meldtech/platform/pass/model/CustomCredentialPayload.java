package org.meldtech.platform.pass.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yubico.webauthn.data.PublicKeyCredentialType;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomCredentialPayload(String id,
                                      String rawId,
                                      PublicKeyCredentialType type,
                                      DeviceAuthenticatorResponse response,
                                      ClientExtensionResponse clientExtensionResults) {
}
