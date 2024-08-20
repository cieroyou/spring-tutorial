package com.sera.tutorial.spring.mqtt.infrastrucrue;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sera.tutorial.spring.mqtt.config.MqttConfig;
import com.sera.tutorial.spring.mqtt.service.Publisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MqttPublisher implements Publisher<MyMessage> {

	private final MqttConfig.MyGateway myGateway;
	private final ObjectMapper objectMapper;
	@Override
	public void publish(String topic, String message) {
		myGateway.publish(topic, message);
	}

	@Override
	public void publish(String topic, MyMessage message) throws JsonProcessingException {
		myGateway.publish(topic, objectMapper.writeValueAsString(message));
	}

}
