FROM maven:3.9.5-eclipse-temurin-17 AS setup
ARG HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn clean verify
COPY src $HOME/src

FROM setup AS test
CMD mvn clean test

FROM setup AS build
RUN mvn clean package -Dmaven.test.skip
