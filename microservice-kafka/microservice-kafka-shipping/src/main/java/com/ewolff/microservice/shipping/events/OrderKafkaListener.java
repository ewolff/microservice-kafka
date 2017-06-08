package com.ewolff.microservice.shipping.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ewolff.microservice.shipping.Shipment;
import com.ewolff.microservice.shipping.ShipmentRepository;

@Component
public class OrderKafkaListener {

	private final Logger log = LoggerFactory.getLogger(OrderKafkaListener.class);

	private ShipmentRepository shipmentRepository;

	public OrderKafkaListener(ShipmentRepository shipmentRepository) {
		super();
		this.shipmentRepository = shipmentRepository;
	}

	@KafkaListener(topics = "order")
	public void order(Shipment shipment) {
		log.info("Revceived shipment " + shipment.getId());
		shipmentRepository.save(shipment);
	}

}
