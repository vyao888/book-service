FROM java:8
EXPOSE 8888
ADD target/book-service-1.0-SNAPSHOT.jar book-service.jar
ENTRYPOINT ["java", "-jar", "book-service.jar"]