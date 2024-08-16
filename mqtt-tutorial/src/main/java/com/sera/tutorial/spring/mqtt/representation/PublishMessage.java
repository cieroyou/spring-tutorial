package com.sera.tutorial.spring.mqtt.representation;

public record PublishMessage(String topic, String message) {
}