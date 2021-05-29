FROM gcr.io/distroless/java:11
WORKDIR /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/mainreader.jar
EXPOSE 8080
CMD ["mainreader.jar"]
