package com.ewolff.microservice.shipping;

import static org.junit.Assert.assertEquals;

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
@SpringBootTest(classes = ShippingTestApp.class, webEnvironment = WebEnvironment.NONE)
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
		assertEquals(countBefore + 1, shipmentRepository.count());
		assertEquals(0L, shipmentRepository.findById(42L).get().getUpdated().getTime());
		shipment = new Shipment(42,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(), new Address("Krischstr. 100", "40789", "Monheim am Rhein"), new ArrayList<ShipmentLine>());
		shipmentService.ship(shipment);
		assertEquals(countBefore + 1, shipmentRepository.count());
		assertEquals(0L, shipmentRepository.findById(42L).get().getUpdated().getTime());
	}

}
