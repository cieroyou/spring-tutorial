package com.sera.tutorial.spring.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@SpringBootApplication
public class MqttTutorialApplication {
	public static void main(String[] args) {
		SpringApplication.run(MqttTutorialApplication.class, args);
	}

}
