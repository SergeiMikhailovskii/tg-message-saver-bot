FROM openjdk:17-jdk-slim AS build

WORKDIR /app
COPY . .

RUN ./gradlew clean build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar tg-saver-bot-1.0-SNAPSHOT.jar

CMD ["java", "-jar", "tg-saver-bot-1.0-SNAPSHOT.jar"]
