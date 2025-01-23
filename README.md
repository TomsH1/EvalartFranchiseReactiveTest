# Demo API - Proyecto Dockerizado

Este proyecto contiene una API RESTful construida con Spring Boot que interactúa con MongoDB. Está completamente dockerizado, lo que facilita su ejecución en cualquier entorno compatible con Docker. A continuación se explica cómo configurar y ejecutar el proyecto, así como los detalles sobre la API y sus endpoints disponibles.

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener las siguientes herramientas instaladas:

- **Docker**: Para construir y ejecutar los contenedores.
- **Docker Compose**: Para manejar la configuración multi-contenedor (MongoDB y la API).
- **Java 17**: Para construir la aplicación en caso de que necesites compilarla localmente.

## Configuración del Proyecto

### Estructura del Proyecto

Este proyecto sigue la siguiente estructura básica de directorios:

/demo-api |-- /src |-- /target |-- /Dockerfile |-- /docker-compose.yml |-- /README.md


### Dockerfile

El archivo `Dockerfile` se utiliza para construir la imagen Docker de la API. Utiliza una imagen base de **OpenJDK 17**, y copia el archivo JAR generado por Maven en la imagen para ejecutarlo.

```dockerfile
# Usar una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR de tu aplicación al contenedor
COPY target/demov-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que tu aplicación escucha (por defecto 8080 en Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```docker-compose.yml
El archivo docker-compose.yml define dos servicios: la API y MongoDB. Usamos Docker Compose para crear y conectar ambos contenedores.
version: '3.8'
services:
  api:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    depends_on:
      - mongodb
    environment:
      SPRING_PROFILES_ACTIVE: docker
    restart: always

  mongodb:
    image: mongo:6.0
    container_name: mongodb
    ports:
      - "27017:27017"
    restart: always
    volumes:
      - mongo-data:/data/db

volumes:
  mongo-data:

```

## Generación de la Aplicación JAR
Para construir el archivo JAR de la aplicación, sigue estos pasos:

Clona el repositorio si aún no lo has hecho.

# Navega al directorio raíz del proyecto y ejecuta el siguiente comando Maven para compilar el proyecto:

mvn clean package
Esto generará el archivo demov-0.0.1-SNAPSHOT.jar dentro del directorio target/.

## Ejecución con Docker
#Para ejecutar la aplicación con Docker, usa los siguientes comandos:
#Construir las imágenes y arrancar los contenedores:

docker-compose build
docker-compose up
Esto construirá las imágenes de Docker y levantará los contenedores para la API y MongoDB. La API estará disponible en el puerto 8080, y MongoDB estará en el puerto 27017.

## Detener los contenedores:

 docker-compose down
 Esto detendrá y eliminará los contenedores, pero mantendrá los datos persistentes en el volumen mongo-data.

Endpoints de la API
Una vez que la aplicación esté en ejecución, puedes acceder a los siguientes endpoints a través de Swagger UI o directamente con herramientas como Postman o cURL.

Acceso a Swagger UI
Swagger UI proporciona una interfaz gráfica donde puedes probar todos los endpoints disponibles de la API. Una vez que la API esté en ejecución, abre tu navegador y ve a la siguiente URL:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui/index.html#/)
