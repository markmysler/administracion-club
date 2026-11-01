# Imagen de club-app: construcción de producción de Vaadin (frontend empaquetado) y ejecución con Java 21.
FROM eclipse-temurin:21-jdk AS construccion
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline -Pproduction || true
COPY src src
RUN ./mvnw -B -q clean package -Pproduction -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=construccion /app/target/administracion-club-6.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
