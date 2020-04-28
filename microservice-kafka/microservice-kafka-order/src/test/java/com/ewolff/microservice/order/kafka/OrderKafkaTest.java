package com.ewolff.microservice.order.kafka;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewolff.microservice.order.OrderApp;
import com.ewolff.microservice.order.OrderTestDataGenerator;
import com.ewolff.microservice.order.logic.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
public class OrderKafkaTest {

	public static Logger logger = LoggerFactory.getLogger(OrderKafkaTest.class);

	@ClassRule
	public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, false, 1, "order");

	@Autowired
	private KafkaListenerBean kafkaListenerBean;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderTestDataGenerator orderTestDataGenerator;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getEmbeddedKafka().getBrokersAsString());
	}

	@Test
	public void orderCreatedSendsKafkaMassage() throws Exception {
		for (int i = 0; i < 3; i++) {
			try {
				for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
					ContainerTestUtils.waitForAssignment(messageListenerContainer,
							1);
				}
			} catch (IllegalStateException ex) {
				logger.warn("Waited unsuccessfully for Kafka assignments");
			}
		}
		int receivedBefore = kafkaListenerBean.getReceived();
		orderService.order(orderTestDataGenerator.createOrder());
		int i = 0;
		while (kafkaListenerBean.getReceived() == receivedBefore && i < 10) {
			Thread.sleep(1000);
			i++;
		}
		assertThat(kafkaListenerBean.getReceived(), is(greaterThan(receivedBefore)));
	}

}
