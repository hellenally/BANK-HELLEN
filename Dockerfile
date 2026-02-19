FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B -Dmaven.wagon.http.retryHandler.count=3
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/bank-hellen-app.jar app.jar
RUN mkdir -p data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]