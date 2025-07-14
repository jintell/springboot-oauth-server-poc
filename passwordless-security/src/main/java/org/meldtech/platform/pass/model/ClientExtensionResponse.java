package org.meldtech.platform.pass.model;

import com.yubico.webauthn.data.ClientExtensionOutputs;
import com.yubico.webauthn.data.ExtensionOutputs;

import java.util.Set;

public record ClientExtensionResponse(Set<String> extensionIds) implements ExtensionOutputs {
    @Override
    public Set<String> getExtensionIds() {
        return extensionIds;
    }
}
