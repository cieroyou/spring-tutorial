package com.sera.tutorial.spring.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class GreetController {

	private final AsyncMailTrigger greeter;

	@GetMapping(value = "/greet/{name}")
	public String greet(@PathVariable(name = "name") String name) throws Exception {
		greeter.asyncGreet(name);
		log.info("Says Name is " + name);
		return name;
	}
}