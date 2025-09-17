# Build Stage
FROM gradle:jdk21-alpine AS build

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x ./gradlew

RUN ./gradlew clean build

# Run Stage
FROM openjdk:21-slim

ENV TZ=Asia/Seoul

COPY --from=build /app/build/libs/gwangju-talent-festival-server-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar" ]