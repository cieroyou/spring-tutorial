package com.sera.tutorial.spring.modulith.orders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@ApplicationModuleTest
@ActiveProfiles("test")
class OrderServiceTest {

	@Autowired
	private  ApplicationEventPublisher publisher;
	@Autowired
	private  OrderService orderService;

	@Test
	void test(){
		String bb = "";
	}
}