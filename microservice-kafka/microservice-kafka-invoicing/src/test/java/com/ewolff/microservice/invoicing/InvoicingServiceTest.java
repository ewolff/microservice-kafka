package com.ewolff.microservice.invoicing;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceTestApp.class, webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
public class InvoicingServiceTest {

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private InvoiceService invoiceService;

	@Test
	public void ensureIdempotencySecondCallIgnored() {
		long countBefore = invoiceRepository.count();
		Invoice invoice = new Invoice(42,
				new Customer(23, "Eberhard", "Wolff", "eberhard.wolff@innoq.com"),
				new Date(0L), new Address("Krischstr. 100", "40789", "Monheim am Rhein"), new ArrayList<InvoiceLine>());
		invoiceService.generateInvoice(invoice);
		assertThat(invoiceRepository.count(), is(countBefore + 1));
		assertThat(invoiceRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
		invoice = new Invoice(42,
				new Customer(23, "Eberhard", "Wolff", "eberhard.wolff@innoq.com"),
				new Date(), new Address("Krischstr. 100", "40789", "Monheim am Rhein"), new ArrayList<InvoiceLine>());
		invoiceService.generateInvoice(invoice);
		assertThat(invoiceRepository.count(), is(countBefore + 1));
		assertThat(invoiceRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
	}

}
