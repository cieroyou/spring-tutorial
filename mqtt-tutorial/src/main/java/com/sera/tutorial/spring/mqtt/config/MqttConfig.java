package com.sera.tutorial.spring.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;

@EnableAsync
@Configuration
@Slf4j
public class MqttConfig {

	private final String subTopic = "test";

	@Value("${mqtt.client-id}")
	private String clientId;

	@Value("${mqtt.hostname}")
	private String hostname;

	@Value("${mqtt.port}")
	private int port;

	private static final String BROKER_URL = "tcp://localhost:1883";
	private static final String MQTT_USERNAME = "admin";
	private static final String MQTT_PASSWORD = "rabbitpassword";
	private static final String MQTT_OUTBOUND_CHANNEL = "mqttOutboundChannel";
	private static final String PUBLISHER = "rabbitmq-publisher";

	@Bean
	public MemoryPersistence memoryPersistence() {
		return new MemoryPersistence();
	}

	public MqttConnectOptions mqttConnectOptions() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] {BROKER_URL});
		options.setCleanSession(true);
		options.setKeepAliveInterval(60);
		options.setAutomaticReconnect(true);
		options.setMaxInflight(10000);
		options.setUserName(MQTT_USERNAME);
		options.setPassword(MQTT_PASSWORD.toCharArray());
		return options;
	}

	@Bean
	public DefaultMqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
		clientFactory.setConnectionOptions(mqttConnectOptions());
		return clientFactory;
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter =
			new MqttPahoMessageDrivenChannelAdapter(BROKER_URL, "SpringBoot-server",
				mqttClientFactory(),
				"hi"); // 192.168.0.254에서 본인 서버 ip 혹은 도메인 주소 넣으시면됩니다.
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	public MessageProducer inbound2() {
		MqttPahoMessageDrivenChannelAdapter adapter =
			new MqttPahoMessageDrivenChannelAdapter(BROKER_URL, "SpringBoot-server2",
				mqttClientFactory(),
				"*.sport.*"); // 192.168.0.254에서 본인 서버 ip 혹은 도메인 주소 넣으시면됩니다.
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = MQTT_OUTBOUND_CHANNEL)
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(PUBLISHER, mqttClientFactory());
		messageHandler.setAsync(true);
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	@MessagingGateway(defaultRequestChannel = MQTT_OUTBOUND_CHANNEL)
	public interface MyGateway {
		void sendToMqtt(String data);

		@Gateway
		void publish(@Header(MqttHeaders.TOPIC) String topic, String data); // (12)
		// void publish(@Header(MqttHeaders.TOPIC) String topic, String data); // (12)
	}

}
