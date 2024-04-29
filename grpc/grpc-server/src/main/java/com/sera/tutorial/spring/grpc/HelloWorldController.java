package com.sera.tutorial.spring.grpc;

import net.devh.boot.grpc.server.service.GrpcService;

import com.sera.tutorial.spring.grpc.helloworld.HelloRequest;
import com.sera.tutorial.spring.grpc.helloworld.HelloResponse;
import com.sera.tutorial.spring.grpc.helloworld.HelloWorldServiceGrpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GrpcService
public class HelloWorldController extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {
	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
		String firstName = request.getFirstName();
		String lastName = request.getLastName();
		log.info("Server - sayHello: " + firstName + " " + lastName);
		HelloResponse response = HelloResponse.newBuilder()
			.setMessage("Hello " + firstName + lastName).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		log.info("Server - sayHello");
	}
}
