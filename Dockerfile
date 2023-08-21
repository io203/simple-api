
FROM eclipse-temurin:17-alpine AS builder
COPY . /src
WORKDIR /src
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:17-alpine
WORKDIR /app
ARG ENV
ENV SPRING_PROFILES_ACTIVE ${ENV}

COPY --from=builder /src/target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","./app.jar"]
