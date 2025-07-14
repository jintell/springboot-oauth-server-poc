# Getting Started

### Passwordless Security

Passkey security (based on WebAuthn and FIDO2) is a passwordless authentication mechanism that enables secure login 
using biometrics, hardware keys (like YubiKeys), or device-based credentials (like Face ID or fingerprint on phones). 
It's phishing-resistant and increasingly adopted in modern applications.

Here's how you can implement Passkey-based security in a Spring Boot application, using WebAuthn libraries such as 
[webauthn4j](https://github.com/webauthn4j/webauthn4j) or [Spring Security WebAuthn](https://github.com/Yubico/java-webauthn-server).

## Overview
### Components

* Relying Party (your server): Issues challenges and stores credentials.
* Client (browser/device): Handles user biometrics or device keys.
* Authenticator (your device): Stores credentials. A Biometric sensor or hardware key (e.g., YubiKey)
* WebAuthn Java library: Handles challenge/response verification.

The following guides illustrate how to use some features concretely:
