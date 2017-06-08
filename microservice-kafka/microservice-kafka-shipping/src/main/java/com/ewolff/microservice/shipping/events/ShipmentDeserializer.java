package com.ewolff.microservice.shipping.events;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.ewolff.microservice.shipping.Shipment;

public class ShipmentDeserializer extends JsonDeserializer<Shipment> {

	public ShipmentDeserializer() {
		super(Shipment.class);
	}

}
