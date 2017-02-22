FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/reify2-0.0.1-SNAPSHOT-standalone.jar /reify2/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/reify2/app.jar"]
