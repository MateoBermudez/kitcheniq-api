# Etapa 1: construir el .jar
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
RUN mvn clean package -DskipTests

# Etapa 2: imagen ligera pero estable para ejecutar la app
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render usa este puerto
ENV PORT=5000
EXPOSE 5000

ENTRYPOINT ["java","-jar","app.jar"]
