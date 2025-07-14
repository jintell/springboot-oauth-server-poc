package org.meldtech.platform.pass.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yubico.webauthn.data.CollectedClientData;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeviceAuthenticatorResponse(String authenticatorData,
                                          String signature,
                                          CollectedClientData clientDataJSON,
                                          String userHandle) {
}
