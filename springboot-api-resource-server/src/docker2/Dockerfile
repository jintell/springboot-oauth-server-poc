FROM eclipse-temurin:17.0.8.1_1-jre-alpine@sha256:da8bbb33e7e61396625b2e47dee1e6f6c164a0321ed4a80b2054a9a398057570
RUN mkdir /app
COPY *.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]