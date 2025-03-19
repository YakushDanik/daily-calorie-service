FROM ubuntu:latest
LABEL authors="Home"
VOLUME /tmp
COPY target/task-management-system-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["top", "-b"]