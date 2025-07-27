FROM openjdk:17-jdk-slim AS build

WORKDIR /app
COPY . .

RUN ./gradlew clean build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar tg-saver-bot-1.0-SNAPSHOT.jar

RUN jar tf build/libs/tg-saver-bot-1.0-SNAPSHOT.jar | grep MainKt

RUN jar tf build/libs/app.jar | grep MainKt

CMD ["java", "-jar", "tg-saver-bot-1.0-SNAPSHOT.jar"]
