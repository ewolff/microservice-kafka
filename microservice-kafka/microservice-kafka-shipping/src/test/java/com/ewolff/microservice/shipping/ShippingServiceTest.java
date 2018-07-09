package com.ewolff.microservice.shipping;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShippingTestApp.class, webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class ShippingServiceTest {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Autowired
	private ShipmentService shipmentService;

	@Test
	public void ensureIdempotencySecondCallIgnored() {
		long countBefore = shipmentRepository.count();
		Shipment shipment = new Shipment(42L,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(0L), new Address("Krischstr. 100", "40789", "Monheim am Rhein"),
				new ArrayList<ShipmentLine>());
		shipmentService.ship(shipment);
		assertThat(shipmentRepository.count(), is(countBefore + 1));
		assertThat(shipmentRepository.findOne(42L).getUpdated().getTime(), equalTo(0L));
		shipment = new Shipment(42,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(), new Address("Krischstr. 100", "40789", "Monheim am Rhein"), new ArrayList<ShipmentLine>());
		shipmentService.ship(shipment);
		assertThat(shipmentRepository.count(), is(countBefore + 1));
		assertThat(shipmentRepository.findOne(42L).getUpdated().getTime(), equalTo(0L));
	}

}
