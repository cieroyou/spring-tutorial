package com.sera.tutorial.spring.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ResourceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

}
