FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# Menggunakan flag untuk menahan gangguan koneksi
RUN mvn dependency:go-offline -B -Dhttp.keepAlive=false -Dmaven.wagon.http.retryHandler.count=3
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/bank-hellen-app.jar app.jar
RUN mkdir -p data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]