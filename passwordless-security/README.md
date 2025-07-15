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


## Benefits of Passkeys
🔒 Phishing-resistant

🧑‍💻 No passwords to steal 

🧑‍💻 Works across devices with synced credentials (Apple, Google, etc.)

The following guides illustrate how to use some features concretely:

* ### To Configure Redis for production and SSL
  `redis.conf settings`  
`
    requirepass yourStrongPasswordHere
    tls-port 6379
    port 0
    tls-cert-file /etc/ssl/redis.crt
    tls-key-file /etc/ssl/redis.key
    tls-ca-cert-file /etc/ssl/ca.crt
`
#### application.yml - Spring Boot Redis Configuration
```
spring: 
  data:
    redis:
      host: redis
      port: 6379
      password: yourStrongPasswordHere
      ssl:
        enabled: true
```
#### Java Configuration Update
```
@Bean
public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("redis", 6379);
    config.setPassword(RedisPassword.of("yourStrongPasswordHere"));

    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .useSsl()
        .build();

    return new LettuceConnectionFactory(config, clientConfig);
}
```
* ### Docker Compose: Spring Boot + Redis + Frontend
  * #### docker-compose.yml
  ```
    version: '3.8'

    services:
        redis:
            image: redis:7.2
            command: [ "redis-server", "/usr/local/etc/redis/redis.conf" ]
            volumes:
                - ./redis/redis.conf:/usr/local/etc/redis/redis.conf
                - ./certs:/etc/ssl
            ports:
                - "6379:6379"
    
      backend:
          build: ./backend
          ports:
            - "8080:8080"
          depends_on:
            - redis
          environment:
            - SPRING_REDIS_HOST=redis
            - SPRING_REDIS_PASSWORD=yourStrongPasswordHere
        
      frontend:
          build: ./frontend
          ports:
            - "3000:3000"
          depends_on:
            - backend
      ```
* ### React Frontend Skeleton (Instead of Plain HTML)
    * Setup Using Vite (bash script)
    ```
    npm create vite@latest frontend --template react
    cd frontend
    npm install
    npm install axios
    ```
    * Example React Component (jsx)
    ```
    import { useState } from "react";
    import axios from "axios";
    
    function App() {
        const [username, setUsername] = useState("");

        const register = async () => {
          const { data: options } = await axios.get(`/webauthn/register/options`, {
            params: { username },
          });
  
          // Prepare options for navigator.credentials.create...
            console.log(options);
          };
  
          const login = async () => {
            const { data: options } = await axios.get(`/webauthn/authenticate/options`, {
              params: { username },
            });

            // Prepare options for navigator.credentials.get...
            console.log(options);
          };
  
          return (
            <div className="App">
              <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Username" />
              <button onClick={register}>Register Passkey</button>
              <button onClick={login}>Login with Passkey</button>
            </div>
          );
    }

    export default App;
    ```