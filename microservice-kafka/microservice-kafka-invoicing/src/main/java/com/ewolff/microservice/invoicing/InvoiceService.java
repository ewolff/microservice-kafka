package com.ewolff.microservice.invoicing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService {

	private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

	private InvoiceRepository invoiceRepository;

	@Autowired
	public InvoiceService(InvoiceRepository invoiceRepository) {
		super();
		this.invoiceRepository = invoiceRepository;
	}

	@Transactional
	public void generateInvoice(Invoice invoice) {
		if (invoiceRepository.existsById(invoice.getId())) {
			log.info("Invoice id {} already exists - ignored", invoice.getId());
		} else {
			invoiceRepository.save(invoice);
		}
	}

}
