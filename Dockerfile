FROM openjdk:8-jre-alpine
WORKDIR /app
MAINTAINER ck_chenwang@163.com
COPY . /app
ENTRYPOINT ["java","-Dspring.profiles.active=test","-jar","pressuretest.jar"]
EXPOSE 8081
