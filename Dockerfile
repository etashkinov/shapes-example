FROM openjdk:15-alpine
VOLUME /tmp
ADD target/shapes-example.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=Usfile:/dev/./urandom","-jar","/app.jar"]
