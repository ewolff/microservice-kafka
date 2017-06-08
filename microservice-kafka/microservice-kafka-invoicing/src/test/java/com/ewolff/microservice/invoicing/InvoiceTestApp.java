package com.ewolff.microservice.invoicing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceTestApp {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(InvoiceTestApp.class);
		app.setAdditionalProfiles("test");
		app.run(args);
	}

}
