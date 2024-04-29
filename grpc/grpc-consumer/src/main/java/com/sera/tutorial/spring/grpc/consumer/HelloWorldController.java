package com.sera.tutorial.spring.grpc.consumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {

	private final HelloWorldGrpcService helloWorldGrpcService;

	@GetMapping("/hello")
	public String printMessage() {
		return helloWorldGrpcService.sendMessage("Sera", "Lee");
	}
}
