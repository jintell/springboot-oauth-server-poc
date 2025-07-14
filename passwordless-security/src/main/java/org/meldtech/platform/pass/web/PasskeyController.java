package org.meldtech.platform.pass.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import com.yubico.webauthn.extension.appid.InvalidAppIdException;
import lombok.extern.slf4j.Slf4j;
import org.meldtech.platform.pass.model.AuthenticationResponsePayload;
import org.meldtech.platform.pass.model.RegistrationResponsePayload;
import org.meldtech.platform.pass.service.PasskeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/webauthn")
@CrossOrigin(origins = "${app.origin}")
public class PasskeyController {

    private final PasskeyService passkeyService;

    public PasskeyController(PasskeyService passkeyService) {
        this.passkeyService = passkeyService;
    }

    @GetMapping("/register/options")
    public String startRegistration(@RequestParam String username) throws InvalidAppIdException, JsonProcessingException {
        return passkeyService.startRegistration(username);
    }

    @PostMapping("/register")
    public ResponseEntity<?> finishRegistration(@RequestBody RegistrationResponsePayload payload) throws RegistrationFailedException {
        return ResponseEntity.ok(passkeyService.finishRegistration(payload));
    }

    @GetMapping("/authenticate/options")
    public String startAuthentication(@RequestParam String username) throws JsonProcessingException {
        return passkeyService.startAuthentication(username);
    }

    @PostMapping("/authenticate")
    public String finishAuthentication(@RequestBody AuthenticationResponsePayload payload) throws AssertionFailedException {
        return passkeyService.finishAuthentication(payload);
    }

}
