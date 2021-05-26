FROM openjdk
WORKDIR /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/project.jar
CMD ["java", "-Xmx64m", "-jar", "/app/project.jar"]
