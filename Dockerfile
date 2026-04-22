# Estágio de Build
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Estágio de Execução
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/nhl-spring-app-0.0.1-SNAPSHOT.jar app.jar
# Expõe a porta (o Render gerencia isso via $PORT)
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
