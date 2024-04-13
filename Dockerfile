FROM ubuntu:latest

RUN apt-get update && apt-get install -y openjdk-16-jdk

EXPOSE 8080

WORKDIR /usr/local/bin/

ADD target/notificationService-0.0.2-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "notificationService-0.0.2-SNAPSHOT.jar"]
