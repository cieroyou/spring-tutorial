package com.sera.tutorial.spring.mqtt.representation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sera.tutorial.spring.mqtt.infrastrucrue.MqttPublisher;
import com.sera.tutorial.spring.mqtt.infrastrucrue.MyMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/publisher")
@RestController
public class PublisherController {
	private final MqttPublisher mqttPublisher;
	// private final MqttConfig.MyGateway myGateway;

	// private MqttOutboundGateway mqttOutboundGateway;
	//
	// public PublisherController(OutboundGateway outboundGateway) {
	// 	this.outboundGateway = outboundGateway;
	// }

	@PostMapping()
	public void publish(@RequestBody PublishMessage request) throws JsonProcessingException {
		// myGateway.sendToMqtt(request.message());
		// myGateway.publish(request.topic(), request.message());
		mqttPublisher.publish(request.topic(), new MyMessage(request.message(), "messagetype"));
	}
}
