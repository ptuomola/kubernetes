FROM gcr.io/distroless/java:11
WORKDIR /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/project.jar
CMD ["project.jar"]
