package com.ewolff.microservice.shipping;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShippingTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class ShipmentKafkaTest {
	
	private final static Logger log = LoggerFactory.getLogger(ShipmentKafkaTest.class);

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Autowired
	public KafkaTemplate<String, String> kafkaTemplate;

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "order");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String brokerAddress = embeddedKafka.getBrokersAsString(); 
		System.setProperty("spring.kafka.bootstrap-servers", brokerAddress);
		log.debug("-- embedded broker address is: " + brokerAddress);
	}

	@Test
	public void orderAreReceived() throws Exception {
		long countBefore = shipmentRepository.count();
		kafkaTemplate.send("order", order());
		int i = 0;
		while (shipmentRepository.count() == countBefore && i < 10) {
			Thread.sleep(1000);
			i++;
		}
		assertThat(shipmentRepository.count(), is(greaterThan(countBefore)));
	}

	private String order() {
		return "{" +
				"  \"id\" : 1," +
				"  \"customer\" : {" +
				"    \"customerId\" : 1," +
				"    \"name\" : \"Wolff\"," +
				"    \"firstname\" : \"Eberhard\"," +
				"    \"email\" : \"eberhard.wolff@gmail.com\"," +
				"    \"street\" : \"Unter den Linden\"," +
				"    \"city\" : \"Berlin\"" +
				"  }," +
				"  \"updated\" : \"2017-04-20T15:42:12.351+0000\"," +
				"  \"shippingAddress\" : {" +
				"    \"street\" : \"Ohlauer Str. 43\"," +
				"    \"zip\" : \"10999\"," +
				"    \"city\" : \"Berlin\"" +
				"  }," +
				"  \"billingAddress\" : {" +
				"    \"street\" : \"Krischerstr. 100\"," +
				"    \"zip\" : \"40789\"," +
				"    \"city\" : \"Monheim am Rhein\"" +
				"  }," +
				"  \"orderLine\" : [ {" +
				"    \"count\" : 42," +
				"    \"item\" : {" +
				"      \"itemId\" : 1," +
				"      \"name\" : \"iPod\"," +
				"      \"price\" : 42.0" +
				"    }" +
				"  } ]," +
				"  \"numberOfLines\" : 1," +
				"  \"_links\" : {" +
				"    \"self\" : {" +
				"      \"href\" : \"http://localhost:8080/order/1\"" +
				"    }," +
				"    \"order\" : {" +
				"      \"href\" : \"http://localhost:8080/order/1\"" +
				"    }" +
				"  }" +
				"}";
	}

}
