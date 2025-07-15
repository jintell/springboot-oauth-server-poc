package org.meldtech.platform.pass.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import com.yubico.webauthn.extension.appid.AppId;
import com.yubico.webauthn.extension.appid.InvalidAppIdException;
import org.meldtech.platform.pass.model.AccessToken;
import org.meldtech.platform.pass.model.AuthenticationResponsePayload;
import org.meldtech.platform.pass.model.RegistrationResponsePayload;
//import org.meldtech.platform.pass.repository.InMemoryCredentialRepository;
import org.meldtech.platform.pass.repository.RedisCredentialRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.yubico.webauthn.data.ResidentKeyRequirement.REQUIRED;

@Service
public class PasskeyService {
    private  final RelyingParty relyingParty;
    private  final RedisCredentialRepository credentialRepo;
//    private  final InMemoryCredentialRepository credentialRepo;
    private final JwtService jwtService;

    private final Map<String, PublicKeyCredentialCreationOptions> registrationOptions = new HashMap<>();
    private final Map<String, PublicKeyCredentialRequestOptions> authOptions = new HashMap<>();

    public PasskeyService(RelyingParty relyingParty,
                          RedisCredentialRepository credentialRepo,
                          JwtService jwtService) {
        this.relyingParty = relyingParty;
        this.credentialRepo = credentialRepo;
        this.jwtService = jwtService;
    }

    public Mono<String> startRegistration(String username) throws InvalidAppIdException, JsonProcessingException {
        UserIdentity user = UserIdentity.builder()
                .name(username)
                .displayName(username)
                .id(new ByteArray(UUID.nameUUIDFromBytes(username.getBytes()).toString().getBytes()))
                .build();

        RegistrationExtensionInputs extensionInputs = RegistrationExtensionInputs.builder()
                .appidExclude(new AppId("https://example.com"))
                .build();

        AuthenticatorSelectionCriteria selectionCriteria = AuthenticatorSelectionCriteria.builder()
                .residentKey(REQUIRED)
                .authenticatorAttachment(AuthenticatorAttachment.PLATFORM)
                .userVerification(UserVerificationRequirement.DISCOURAGED)
                .build();

        PublicKeyCredentialCreationOptions options = relyingParty.startRegistration(
                StartRegistrationOptions.builder()
                        .user(user)
                        .extensions(extensionInputs)
                        .authenticatorSelection(selectionCriteria)
                        .build()
        );

        registrationOptions.put(username, options);
        return Mono.justOrEmpty(options.toCredentialsCreateJson());
    }

    public Mono<String> finishRegistration(RegistrationResponsePayload payload) throws RegistrationFailedException {
        var result = relyingParty.finishRegistration(FinishRegistrationOptions.builder()
                .request(registrationOptions.get(payload.username()))
                .response(payload.credential())
                .build());


        RegisteredCredential regCred = RegisteredCredential.builder()
                .credentialId(payload.credential().getId())
                .userHandle(new ByteArray(UUID.nameUUIDFromBytes(payload.username().getBytes()).toString().getBytes()))
                .publicKeyCose(result.getPublicKeyCose())
                .signatureCount(result.getSignatureCount())
                .build();

        credentialRepo.save(payload.username(), regCred);
        return Mono.justOrEmpty("Registered successfully");
    }

    public Mono<String> startAuthentication(String username) throws JsonProcessingException {
        PublicKeyCredentialRequestOptions options = relyingParty.startAssertion(
                StartAssertionOptions.builder()
                        .username(username)
                        .build()
        ).getPublicKeyCredentialRequestOptions();

        // Store options for later
        authOptions.put(username, options);

        return Mono.justOrEmpty(options.toCredentialsGetJson());
    }

    public Mono<AccessToken> finishAuthentication(AuthenticationResponsePayload payload) throws AssertionFailedException {
        PublicKeyCredentialRequestOptions request = authOptions.get(payload.username());
        AssertionResult result = relyingParty.finishAssertion(
                FinishAssertionOptions.builder()
                        .request(buildFinishAssertionOptions(request))
                        .response(payload.credential())
                        .build()
        );

        return result.isSuccess() ? Mono.justOrEmpty(jwtService.generateToken(payload.username())) : Mono.empty();
    }

    private AssertionRequest buildFinishAssertionOptions(PublicKeyCredentialRequestOptions request) {
        return AssertionRequest.builder()
                .publicKeyCredentialRequestOptions(request)
                .build();
    }

}
