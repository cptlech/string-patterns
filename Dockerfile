FROM eclipse-temurin:17-jre-jammy
COPY build/libs/ll-string-patterns.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]