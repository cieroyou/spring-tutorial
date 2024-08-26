package com.sera.tutorial.spring.modulith.orders;

// @Externalized(target = "orders")
public record OrderPlacedEvent(
	Long order,
	int quantity,
	Long product
) {
}