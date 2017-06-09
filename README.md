Microservice Kafka Sample
==================

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
- Kafka
- Zookeeper
- Postgres
- Docker Compose to link the containers.

How To Run
----------

The demo can be run with
[Docker Machine and Docker Compose](docker/README.md).

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
