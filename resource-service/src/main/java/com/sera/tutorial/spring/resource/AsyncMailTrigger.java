package com.sera.tutorial.spring.resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsyncMailTrigger {

	@Async
	public void asyncGreet(String name) throws Exception {
		log.info("Trigger mail in a New Thread ");
		log.info("greets before sleep: " + name);
		Thread.sleep(3000);
		log.info("greets: " + name);
	}

}