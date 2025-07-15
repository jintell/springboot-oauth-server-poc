package org.meldtech.platform.pass.config;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class WebAuthnConfig {
    @Value("${app.rp-id}")
    private String rpId;

    @Value("${app.rp-name}")
    private String rpName;

    @Value("${app.origin}")
    private String origin;

    @Bean
    public RelyingParty relyingParty(CredentialRepository credentialRepository) {
        return RelyingParty.builder()
                .identity(RelyingPartyIdentity.builder()
                        .id(rpId)
                        .name(rpName)
                        .build())
                .credentialRepository(credentialRepository)
                .origins(Set.of(origin))
                .build();
    }

}

