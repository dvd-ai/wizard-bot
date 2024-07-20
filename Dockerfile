FROM openjdk:17-alpine

ADD ./build/libs/* /app/

WORKDIR /app

ENTRYPOINT java -jar ./*.jar