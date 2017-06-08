package com.ewolff.microservice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderTestApp {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(OrderTestApp.class);
		app.setAdditionalProfiles("test");
		app.run(args);
	}

}
