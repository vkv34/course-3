FROM openjdk:22
EXPOSE 8080:8080
#COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar
COPY ./build/libs/server.jar /server/server.jar
ENTRYPOINT ["java","-jar","/server/server.jar"]

#FROM gradle:8.6.0-jdk21-alpine AS build
#COPY --chown=gradle:gradle ./server/ /home/gradle/src
#RUN ls /home/gradle/src
#COPY --chown=gradle:gradle ./gradle/ /home/gradle/src/gradle
#WORKDIR /home/gradle/src
#RUN gradle buildFatJar --no-daemon
#
#FROM openjdk:11
#EXPOSE 8080:8080
#RUN mkdir /app
#COPY --from=build /home/gradle/src/build/libs/*.jar /app/server.jar
#ENTRYPOINT ["java","-jar","/app/server.jar"]