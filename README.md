Microservice Kafka Sample
==================

[Deutsche Anleitung zum Starten des Beispiels](WIE-LAUFEN.md)

This is a sample to show how Kafka can be used for the communication
between microservices.

The project creates Docker containers.

It uses three microservices:
- Order to create orders. This services sends messages to Kafka. It
  uses the `KafkaTemplate`.
- Shipment receives the orders and extract the
  information needed to ship the items.
- Invoicing receives the messages, too. It extracts all information to send
out an invoice. It uses `@KafkaListener` just like Shipment.

This is done using a topic order. It has five partitions. Shipment and
invoicing each have a separate consumer group. So multiple instances
of shipment and invoicing can be run. Each instance would get specific
events.

Technologies
------------

- Spring Boot
- Spring Kafka
- Apache httpd
- Kafka
- Zookeeper
- Postgres
- Docker Compose to link the containers.

How To Run
----------

See [How to run](HOW-TO-RUN.md) for details.

Once you create an order in the order application, after a while the
invoice and the shipment should be shown in the other applications.

Remarks on the Code
-------------------

The microservices are: 
- [microservice-kafka-order](microservice-kafka/microservice-kafka-order) to create the orders
- [microserivce-kafka-shipping](microservice-kafka/microservice-kafka-shipping) for the shipping
- [microservice-kafka-invoicing](microservice-kafka/microservice-kafka-invoicing) for the invoices

The data of an order is copied - including the data of the customer
and the items. So if a customer or item changes in the order system
this does not influence existing shipments and invoices. It would be
odd if a change to a price would also change existing invoices. Also
only the information needed for the shipment and the invoice are
copied over to the other systems.

The Order microservice uses Spring's `KafkaTemplate` to send message
while the other two microservices use the annotation `@KafkaListener`
on the methods that should be called if a new record comes in. All
records are put in the `order` topic. It has five partitions to allow
for scalability.

For tests an embedded Kafka server is used. A `@ClassRule` starts
it. And a method annotated with `@BeforeClass` configures Spring Kafka
to use the embedded Kafka server.

The orders are serialized as JSON.
So the `Order` object of the order microservice is serialized as a JSON data structure.
The other two microservices just
read the data they need for shipping and invoicing. So the invoicing microservices reads the `Invoice`object and the 
delivery microservice the `Delivery`object.
This avoids code dependencies between the
microservices. `Order` contains all the data for `Invoice` as well as `Delivery`.
JSON serialization is flexible. So when an `Order` is deserialized into `Invoice` and `Delivery` just the needed data is read.
The additional data is just ignored.

There are three Docker container for the microservices. The other
Docker containers are for Apache httpd, Kafka, Zookeeper and Postgres.

Incoming http request are handled by the Apache httpd server. It is
available at port 8080 of the Docker host
e.g. <http://localhost:8080>.  HTTP requests are forwarded to the
microservices. Kafka is used for the communication between the
microservices. Kafka needs Zookeeper to coordinate instances. Postgres
is used by all microservices to store data. Each microservices uses
its own database in the Postgres instance so they are decoupled in
that regard.

You can scale the listener with e.g. `docker-compose scale
shipping=2`. The logs (`docker logs
mskafka_shipping_1`) will show which partitions the instances listen
to and which records they handle.

You can also start a shell on the Kafka server `docker exec -it
mskafka_kafka_1 /bin/sh` and then take a look at the records in the
topic using `kafka-console-consumer.sh --bootstrap-server kafka:9092
--topic order --from-beginning`.
