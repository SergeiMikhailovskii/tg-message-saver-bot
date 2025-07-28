FROM openjdk:21-jdk-slim AS build

WORKDIR /app
COPY . .

RUN ./gradlew clean build -x test

RUN ls build/libs && jar tf build/libs/*.jar | grep MainKt

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar tg-saver-bot-1.0-SNAPSHOT.jar

CMD ["java", "-jar", "tg-saver-bot-1.0-SNAPSHOT.jar"]
