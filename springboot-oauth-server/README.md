# Getting Started

### Spring Authorization Server

This Application is based on Spring Boot app using spring boot 3.1.3 and maven and java 17.

It uses the latest maven artifact spring-boot-starter-oauth2-authorization-server and it is configured run on port 9000. 
This authorization server also uses InMemoryUserDetailsManager with 2 users: 
The 1st is `simpleuser` with the role `USER`, and the 2nd is `adminuser` with the role `ADMIN`.

It Also configured 3 clients for use which are:
- **client_spa**: for reactjs/angula app, and should use Grant Type = PKCE Enhanced Authorization Code
- **client_serverapp**: for server side app, and should use Grant Type = Client Credentials
- **client_mobile**: for mobile app, and should use Grant Type = Authorization Code

### Guides

The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [OAuth2 Authorization Server](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.security.oauth2.authorization-server)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.reactive)

