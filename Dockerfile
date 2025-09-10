# Imagen base con JDK 17
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app
COPY . .

# Compilar el proyecto
RUN ./mvnw clean package -DskipTests

# Imagen final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiar el jar generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 1234

# Comando de inicio
ENTRYPOINT ["java","-jar","app.jar"]