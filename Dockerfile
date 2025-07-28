FROM openjdk:21-jdk-slim AS build

WORKDIR /app
COPY . .

RUN ./gradlew shadowJar -x test

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/tg-saver-bot-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
