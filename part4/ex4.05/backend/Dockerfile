FROM openjdk
WORKDIR /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/backend.jar
CMD ["java", "-Xmx64m", "-jar", "backend.jar"]
