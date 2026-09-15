FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY pom.xml mvnw .
COPY .mvn .mvn
COPY backend /app
RUN ./mvnw -q -DskipTests package
EXPOSE 8080
CMD ["java","-jar","target/model-engine-backend-0.0.1-SNAPSHOT.jar"]
