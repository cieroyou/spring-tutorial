package com.sera.tutorial.spring.mqtt.infrastrucrue;

import com.sera.tutorial.spring.mqtt.service.PublishMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyMessage implements PublishMessage {
	private String message;
	private String type;
}
