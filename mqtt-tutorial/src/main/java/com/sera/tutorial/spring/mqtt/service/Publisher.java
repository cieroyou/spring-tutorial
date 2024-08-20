package com.sera.tutorial.spring.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Publisher<T extends PublishMessage> {

	void publish(String topic, String message);

	void publish(String topic, T message) throws JsonProcessingException;
}
