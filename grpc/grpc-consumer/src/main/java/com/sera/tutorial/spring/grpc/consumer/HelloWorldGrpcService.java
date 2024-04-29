package com.sera.tutorial.spring.grpc.consumer;

import org.springframework.stereotype.Service;

import net.devh.boot.grpc.client.inject.GrpcClient;

import com.sera.tutorial.spring.grpc.helloworld.HelloResponse;
import com.sera.tutorial.spring.grpc.helloworld.HelloWorldServiceGrpc;

@Service
public class HelloWorldGrpcService {
	@GrpcClient("hello")
	HelloWorldServiceGrpc.HelloWorldServiceBlockingStub blockingStub;

	public String sendMessage(String firstName, String lastName) {
		HelloResponse response = blockingStub.sayHello(
			com.sera.tutorial.spring.grpc.helloworld.HelloRequest.newBuilder()
				.setFirstName(firstName)
				.setLastName(lastName)
				.build());
		return response.getMessage();
	}

}
