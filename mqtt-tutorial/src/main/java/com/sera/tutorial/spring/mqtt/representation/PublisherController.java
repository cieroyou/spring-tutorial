package com.sera.tutorial.spring.mqtt.representation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sera.tutorial.spring.mqtt.config.MqttConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/publisher")
@RestController
public class PublisherController {
	private final MqttConfig.MyGateway myGateway;

	// private MqttOutboundGateway mqttOutboundGateway;
	//
	// public PublisherController(OutboundGateway outboundGateway) {
	// 	this.outboundGateway = outboundGateway;
	// }

	@PostMapping()
	public void publish(@RequestBody PublishMessage request) {
		// myGateway.sendToMqtt(request.message());
		myGateway.publish(request.topic(), request.message());
	}
}
