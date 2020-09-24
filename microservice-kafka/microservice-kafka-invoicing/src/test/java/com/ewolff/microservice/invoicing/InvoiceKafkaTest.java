package com.ewolff.microservice.invoicing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceTestApp.class, webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
@ContextConfiguration(initializers = { InvoiceKafkaTest.Initializer.class })
public class InvoiceKafkaTest {

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	public KafkaTemplate<String, String> kafkaTemplate;

	@ClassRule
	public static KafkaContainer kafkaContainer = new KafkaContainer();

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of("spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers())
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}
	
	@Test
	public void orderAreReceived() throws Exception {
		long countBefore = invoiceRepository.count();
		kafkaTemplate.send("order", order());
		int i = 0;
		while (invoiceRepository.count() == countBefore && i < 10) {
			Thread.sleep(1000);
			i++;
		}
		assertThat(invoiceRepository.count(), is(greaterThan(countBefore)));
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
