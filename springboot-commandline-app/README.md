# Getting Started

### Spring Boot Console Client App

This Application is a Spring Boot console app using spring boot 3.1.3 and maven and java 17.

It uses the latest maven artifact `spring-boot-starter-webflux` and it is configured run on port 8081.
It uses the Client Credentials for authentication(client_serverapp) and invoke "GET /hello-admin" endpoint on the Api
resource server and print the response in the console.

### Build the artifact

change to the authorization directory and then build with:

`./mvnw clean package`

### Run the Client App Server

`java -jar target/springboot-commandline-app-0.0.1-SNAPSHOT.jar`

### Alternatively, Build and Run with a Single maven command.
*This option will not build the artifact (**springboot-commandline-app-0.0.1-SNAPSHOT.jar**) for you*

`./mvnw spring-boot:run`

### Stop the server

*Use Control+C to terminate the running server*


### Guides

The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.reactive)


