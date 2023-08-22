FROM eclipse-temurin:17-alpine AS builder
COPY . /src
WORKDIR /src
RUN ./mvnw clean package -DskipTests

### runtime
FROM eclipse-temurin:17-alpine
WORKDIR /app
ARG PROFILE_ENV
ENV SPRING_PROFILES_ACTIVE $PROFILE_ENV

COPY --from=builder /src/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","./app.jar"]