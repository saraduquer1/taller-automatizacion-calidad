# Imagen base con Java 21 (alpine para que sea ligera)
FROM eclipse-temurin:21-jdk-alpine

# Crear directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo .jar al contenedor
COPY gestor-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto (ajusta si tu app usa otro)
EXPOSE 8080

# Comando para ejecutar el .jar
ENTRYPOINT ["java", "-jar", "app.jar"]
