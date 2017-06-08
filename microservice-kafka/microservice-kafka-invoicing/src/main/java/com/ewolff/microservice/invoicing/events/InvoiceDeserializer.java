package com.ewolff.microservice.invoicing.events;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.ewolff.microservice.invoicing.Invoice;

public class InvoiceDeserializer extends JsonDeserializer<Invoice> {

	public InvoiceDeserializer() {
		super(Invoice.class);
	}

}
