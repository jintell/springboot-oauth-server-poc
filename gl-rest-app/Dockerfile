# Use lightweight OpenJDK image
FROM openjdk:21-slim
LABEL authors="josleke"

# Set work directory
WORKDIR /app

# Add compiled JAR
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Expose port
EXPOSE 9100

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]


