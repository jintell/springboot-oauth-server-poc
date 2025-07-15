package org.meldtech.platform.pass.repository;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.PublicKeyCredentialType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class RedisCredentialRepository implements CredentialRepository {

    private final RedisTemplate<String, RegisteredCredential> redisCredentialTemplate;

    private static final String PREFIX = "passkey:";

    public RedisCredentialRepository(RedisTemplate<String, RegisteredCredential> redisCredentialTemplate) {
        this.redisCredentialTemplate = redisCredentialTemplate;
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        RegisteredCredential credential = redisCredentialTemplate.opsForValue().get(PREFIX + username);
        return credential != null ? Optional.of(credential.getUserHandle()) : Optional.empty();
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        // Implement lookup logic if needed
        return Optional.of(userHandle.getBase64Url());
    }

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        RegisteredCredential credential = redisCredentialTemplate.opsForValue().get(PREFIX + username);
        if (credential != null) {
            return Set.of(PublicKeyCredentialDescriptor.builder()
                    .id(credential.getCredentialId())
                    .type(PublicKeyCredentialType.PUBLIC_KEY)
                    .build());
        }
        return Collections.emptySet();
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        // Implement lookup logic if needed
        // Get the Username from the userHandle
        return getRegisteredCredential(credentialId);
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        // Implement lookup logic if needed
        Optional<RegisteredCredential> credential = getRegisteredCredential(credentialId);
        return credential.map(Collections::singleton).orElse(Collections.emptySet());
    }

    private Optional<RegisteredCredential> getRegisteredCredential(ByteArray credentialId) {
        return redisCredentialTemplate.keys(PREFIX + "*")
                .stream()
                .map(key -> redisCredentialTemplate.opsForValue().get(key)).filter(Objects::nonNull)
                .filter(registeredCredential -> registeredCredential.getCredentialId().equals(credentialId))
                .findFirst();
    }

    public void save(String username, RegisteredCredential credential) {
        redisCredentialTemplate.opsForValue().set(PREFIX.concat(username), credential);
    }
}

