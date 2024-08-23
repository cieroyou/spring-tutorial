package com.sera.tutorial.spring.modulith;

import org.springframework.modulith.ApplicationModuleInitializer;
import org.springframework.stereotype.Component;

@Component
public class ModuleInitializer implements ApplicationModuleInitializer {

	@Override
	public void initialize() {
		System.out.println("initializing module");
	}
}