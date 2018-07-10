package com.ewolff.microservice.order.logic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

	private OrderRepository orderRepository;

	private KafkaTemplate<String, Order> kafkaTemplate;

	@Autowired
	private OrderService(OrderRepository orderRepository, KafkaTemplate<String, Order> kafkaTemplate) {
		super();
		this.orderRepository = orderRepository;
		this.kafkaTemplate = kafkaTemplate;
	}

	public Order order(Order order) {
		if (order.getNumberOfLines() == 0) {
			throw new IllegalArgumentException("No order lines!");
		}
		order.setUpdated(new Date());
		Order result = orderRepository.save(order);
		fireOrderCreatedEvent(order);
		return result;
	}

	private void fireOrderCreatedEvent(Order order) {
		kafkaTemplate.send("order", order.getId() + "created", order);
	}

	public double getPrice(long orderId) {
		return orderRepository.findById(orderId).get().totalPrice();
	}

}
