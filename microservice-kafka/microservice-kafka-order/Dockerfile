FROM openjdk:10.0.2-jre-slim
COPY target/microservice-kafka-order-0.0.1-SNAPSHOT.jar .
CMD /usr/bin/java -Xmx400m -Xms400m -jar microservice-kafka-order-0.0.1-SNAPSHOT.jar
EXPOSE 8080
