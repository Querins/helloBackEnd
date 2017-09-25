package com.test.helloBackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class HelloBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloBackEndApplication.class, args);
	}
}
