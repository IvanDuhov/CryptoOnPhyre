ARG BUILDER_IMAGE=gradle:7.2.0-jdk11-alpine
ARG RUNTIME_IMAGE=openjdk:11-jre-slim

# Build the application (Stage 1)
FROM $BUILDER_IMAGE AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon


#Runtime image (stage 2)
FROM $RUNTIME_IMAGE

ENV APPLICATION_NAME=cryptoapi
# Just temporarily for debugging purposes
EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/${APPLICATION_NAME}.jar /app/${APPLICATION_NAME}.jar

CMD java -jar /app/${APPLICATION_NAME}.jar
