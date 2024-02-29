FROM eclipse-temurin:21-jre-alpine
RUN mkdir /app
COPY /target/twitch-microservice-0.0.1-SNAPSHOT.jar /app/twitch.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "twitch.jar"]