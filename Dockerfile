FROM gradle:8.5.0-jdk11-focal AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11
run mkdir /app
COPY --from=build /home/gradle/src/build/libs/secret-santa-bot-*-all.jar /app/secret-santa-bot.jar
ENTRYPOINT ["java", "-jar", "/app/secret-santa-bot.jar"]
