IMAGE_TAG = 3.5-jdk-8-alpine

PWD := $(shell pwd)

.PHONY: all

all: clean build run

build:
	docker run -ti --rm -v $(PWD)/microservice-kafka:/app maven:$(IMAGE_TAG) /bin/sh -c 'cd /app && mvn clean package package -Dmaven.test.skip=true'
run:
	cd docker && docker-compose build && docker-compose up -d
clean:  clean-build

clean-build:
	sudo rm -rf microservice-kafka/microservice-kafka-invoicing/taget 
	sudo rm -rf microservice-kafka/microservice-kafka-order/taget 
	sudo rm -rf microservice-kafka/microservice-kafka-shipping/taget 
