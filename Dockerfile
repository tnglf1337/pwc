FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /

COPY pom.xml .

# Lade alle Abhängigkeiten, damit sie gecached werden können (schnellerer Build bei späteren Änderungen)
RUN mvn dependency:go-offline -B

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]