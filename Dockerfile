FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:21-slim
VOLUME /tmp
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
