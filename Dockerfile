FROM eclipse-temurin:17-alpine AS builder
WORKDIR /build
COPY mvnw .
COPY .mvn ./.mvn
COPY pom.xml .
# RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline
RUN --mount=type=cache,target=/root/.m2  ./mvnw dependency:resolve -U

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 ./mvnw clean package -DskipTests
RUN java -Djarmode=layertools -jar target/*.jar extract

### runtime
FROM eclipse-temurin:17.0.2_8-jre
WORKDIR /app
ARG PROFILE_ENV
ENV SPRING_PROFILES_ACTIVE $PROFILE_ENV


COPY --from=builder  /build/dependencies/ ./
COPY --from=builder  /build/spring-boot-loader/ ./
COPY --from=builder  /build/snapshot-dependencies/ ./
COPY --from=builder  /build/application/ ./
EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]