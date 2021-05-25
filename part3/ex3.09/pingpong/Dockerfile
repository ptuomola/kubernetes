FROM gcr.io/distroless/java:11
WORKDIR /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/pingpong.jar
CMD ["pingpong.jar"]
