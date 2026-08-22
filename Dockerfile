# ---- Build stage ----
# Pinned to JDK 8 to match this project's <java.version> in pom.xml, rather
# than relying on Railway's auto-detected buildpack picking a newer JDK.
FROM eclipse-temurin:8-jdk AS build
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml suppression.xml ./
RUN chmod +x mvnw

# Warm the local dependency cache before copying source, so source-only
# changes don't bust the whole dependency download on rebuild.
RUN ./mvnw -B -q -Ddependency-check.skip=true dependency:go-offline || true

COPY src ./src

# -Ddependency-check.skip=true: the OWASP dependency-check plugin needs an
# NVD API key (via -Dnvd.api.key) that isn't available in this build
# environment, and its "check" goal is bound to the verify phase anyway, so
# `package` alone wouldn't reach it -- this is just a safety net.
RUN ./mvnw -B -DskipTests -Ddependency-check.skip=true clean package

# ---- Runtime stage ----
FROM eclipse-temurin:8-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Activates application-railway.properties (disables the app's own SSL,
# listens on Railway's $PORT, mounts everything under /ssl-demo). Local runs
# (mvnw spring-boot:run, your IDE, grading) don't set this and keep using
# application.properties with the self-signed keystore on 8443 as before.
ENV SPRING_PROFILES_ACTIVE=railway

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
