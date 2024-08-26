package com.sera.tutorial.spring.modulith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableRetry
@EnableAsync
@SpringBootApplication
public class ModulithTutorialApplication {
	public static void main(String[] args) {
		SpringApplication.run(ModulithTutorialApplication.class, args);
	}

}
