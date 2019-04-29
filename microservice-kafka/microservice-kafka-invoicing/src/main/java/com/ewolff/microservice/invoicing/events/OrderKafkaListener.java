package com.ewolff.microservice.invoicing.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.ewolff.microservice.invoicing.Invoice;
import com.ewolff.microservice.invoicing.InvoiceService;

@Component
public class OrderKafkaListener {

	private final Logger log = LoggerFactory.getLogger(OrderKafkaListener.class);

	private InvoiceService invoiceService;

	public OrderKafkaListener(InvoiceService invoiceService) {
		super();
		this.invoiceService = invoiceService;
	}

	@KafkaListener(topics = "order")
	public void order(Invoice invoice, Acknowledgment acknowledgment) {
		log.info("Received invoice " + invoice.getId());
		invoiceService.generateInvoice(invoice);
		acknowledgment.acknowledge();
	}

}
