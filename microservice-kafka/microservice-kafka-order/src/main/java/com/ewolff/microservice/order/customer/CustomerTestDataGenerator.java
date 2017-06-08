package com.ewolff.microservice.order.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerTestDataGenerator {

	private final CustomerRepository customerRepository;

	@Autowired
	public CustomerTestDataGenerator(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@PostConstruct
	public void generateTestData() {
		customerRepository
				.save(new Customer("Eberhard", "Wolff", "eberhard.wolff@gmail.com", "Unter den Linden", "Berlin"));
		customerRepository.save(new Customer("Rod", "Johnson", "rod@somewhere.com", "Market Street", "San Francisco"));
	}

}
