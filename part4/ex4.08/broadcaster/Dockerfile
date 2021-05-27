FROM openjdk
WORKDIR /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/broadcaster.jar
CMD ["java", "-Xmx64m", "-jar", "broadcaster.jar"]
