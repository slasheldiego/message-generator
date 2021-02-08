# First stage: JDK 11 with modules required for Spring Boot
FROM gradle:6.7.1-jdk11 AS build

RUN echo 'Diego'
