package com.sera.tutorial.spring.jpa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class OrderServiceTest {

	@InjectMocks
	OrderService orderService;

	@Mock
	OrderItemService orderItemService;
	@Test
	void registerOrder() {
		// stubbing
		when(orderItemService.createOrderItem()).thenReturn("createOrderItem");
		String result = orderService.registerOrder();

		assertEquals("createOrderItem", result);
	}
}