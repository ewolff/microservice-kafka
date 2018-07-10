package com.ewolff.microservice.order.customer;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class CustomerFormatter implements Formatter<Customer> {

	private CustomerRepository customerRepository;

	@Autowired
	public CustomerFormatter(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public String print(Customer customer, Locale locale) {
		return customer.getCustomerId().toString();
	}

	@Override
	public Customer parse(String text, Locale locale) throws ParseException {
		return customerRepository.findById(Long.parseLong(text)).get();
	}

}
