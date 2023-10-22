# This is a multistage Docker build
# Create the Java artifact. Using Maven
FROM maven:3-eclipse-temurin-17 AS build
LABEL authors="josleke"
RUN mkdir /usr/src/project
COPY . /usr/src/project
WORKDIR /usr/src/project
RUN mvn package -DskipTests
RUN jar xf target/JavaCoffeeShop.jar
RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 17  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    target/JavaCoffeeShop.jar > deps.info
RUN jlink \
    --add-modules $(cat deps.info) \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /myjre
FROM debian:bookworm-slim
ENV JAVA_HOME /user/java/jdk17
ENV PATH $JAVA_HOME/bin:$PATH
COPY --from=build /myjre $JAVA_HOME
RUN mkdir /project
COPY --from=build /usr/src/project/target/JavaCoffeeShop.jar /project/
WORKDIR /project
ENTRYPOINT ["java", "-jar", "JavaCoffeeShop.jar"]