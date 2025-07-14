package org.meldtech.platform.pass.model;

public record AuthResponsePayload(String username, CustomCredentialPayload credential) {
}
