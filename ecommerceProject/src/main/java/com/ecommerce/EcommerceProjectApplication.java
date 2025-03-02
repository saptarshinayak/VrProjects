package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.ecommerce.model")
@SpringBootApplication
public class EcommerceProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceProjectApplication.class, args);
	}

}
