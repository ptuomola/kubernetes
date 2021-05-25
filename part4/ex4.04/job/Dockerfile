FROM alpine:3.7
RUN apk update && apk add curl
WORKDIR /app
COPY postTodo.sh /app/postTodo.sh
CMD ["/app/postTodo.sh"]
