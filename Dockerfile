# First stage: JDK build
FROM maven:3.6.3-openjdk-17-slim AS prepare-build

# Create buildtime user and directory
RUN useradd -m appbuilder
RUN mkdir /build && \
    chown -R appbuilder /build
WORKDIR /build
USER appbuilder

# Copy rest of the dependencies.
COPY --chown=appbuilder pom.xml .
RUN mvn dependency:go-offline

FROM prepare-build AS build-app
USER appbuilder

# Copy the source code. Note expect to have .dockerignore that ignores
COPY --chown=appbuilder . .

## Do Vaadin  production build
RUN mvn clean install -Pproduction

# Create runtime from lightweight jdk-slim image
FROM openjdk:17-jdk-slim AS runtime

# Create runtime user and directory
RUN useradd -m appuser
RUN mkdir /app && \
    chown -R appuser /app
WORKDIR /app

# Copy the native binary from the build stage
COPY --from=build-app --chown=appuser /build/target/vaadin-cors-sample-*.jar /app/app.jar

# Run the application
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]