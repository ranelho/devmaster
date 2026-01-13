package com.devmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.devmaster")
@EntityScan(basePackages = "com.devmaster")
public class DevmasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevmasterApplication.class, args);
	}

}
