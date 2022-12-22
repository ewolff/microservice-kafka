package com.ewolff.microservice.order.customer;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CustomerTestDataGenerator {

	private final CustomerRepository customerRepository;

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
