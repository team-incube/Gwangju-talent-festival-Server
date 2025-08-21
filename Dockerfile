FROM openjdk:21-slim

COPY ./build/libs/gwangju-talent-festival-server-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar" ]