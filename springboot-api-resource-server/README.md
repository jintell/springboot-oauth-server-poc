# Getting Started

### Spring API Resource Server

This Application is based on Spring Boot app using spring boot 3.1.3 and maven and java 17.

It uses the latest maven artifact `spring-boot-starter-oauth2-resource-server` and it is configured run on port 8080.
It also uses `spring-security-oauth2-jose` artifact from **spring security** to deal with the JWTs

This API have the following endpoints:
- [GET /info](): *public endpoint*
- [GET /hello](): *protected endpoint, requires any authenticated user*
- [GET /hello-admin](): *protected endpoint, requires authenticated user with role ADMIN*

### Build the artifact

change to the authorization directory and then build with:

`./mvnw clean package`

### Run the Resource Api Server

`java -jar target/springboot-api-resource-server-0.0.1-SNAPSHOT.jar`

### Alternatively, Build and Run with a Single maven command.
*This option will not build the artifact (**springboot-api-resource-server-0.0.1-SNAPSHOT.jar**) for you*

`./mvnw spring-boot:run`

### Stop the server

*Use Control+C to terminate the running server*

### Guides

The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.reactive)

