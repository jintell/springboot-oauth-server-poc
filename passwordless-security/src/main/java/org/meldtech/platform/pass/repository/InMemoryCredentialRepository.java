package org.meldtech.platform.pass.repository;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.PublicKeyCredentialType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryCredentialRepository implements CredentialRepository {
    private final Map<ByteArray, RegisteredCredential> credentialStore = new ConcurrentHashMap<>();

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return credentialStore.values().stream()
                .filter(c -> c.getUserHandle().getBase64Url().equals(username))
                .findFirst()
                .map(RegisteredCredential::getUserHandle);
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return Optional.of(userHandle.getBase64Url());
    }

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        return credentialStore.values().stream()
                .filter(c -> c.getUserHandle().getBase64Url().equals(username))
                .map(RegisteredCredential::getCredentialId)
                .map(id -> PublicKeyCredentialDescriptor.builder()
                        .id(id)
                        .type(PublicKeyCredentialType.PUBLIC_KEY)
                        .build())
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        return Optional.ofNullable(credentialStore.get(credentialId));
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        RegisteredCredential credential = credentialStore.get(credentialId);
        return Objects.isNull(credential) ? Set.of() : Set.of(credentialStore.get(credentialId));
    }


    public void save(RegisteredCredential credential) {
        credentialStore.put(credential.getCredentialId(), credential);
    }
}
