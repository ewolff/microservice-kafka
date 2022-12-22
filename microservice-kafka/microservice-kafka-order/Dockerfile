FROM openjdk:18.0.2.1-slim
COPY target/microservice-kafka-order-0.0.1-SNAPSHOT.jar .
CMD java -Xmx400m -Xms400m  -XX:TieredStopAtLevel=1 -noverify -jar microservice-kafka-order-0.0.1-SNAPSHOT.jar
EXPOSE 8080
