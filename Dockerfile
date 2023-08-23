FROM eclipse-temurin:17-alpine AS builder
WORKDIR /build
COPY mvnw .
COPY .mvn ./.mvn
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline
# RUN --mount=type=cache,target=/root/.m2  ./mvnw dependency:resolve -U

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 ./mvnw clean package -DskipTests


### runtime
FROM eclipse-temurin:17-alpine
WORKDIR /app
ARG PROFILE_ENV
ENV SPRING_PROFILES_ACTIVE $PROFILE_ENV

COPY --from=builder /build/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","./app.jar"]