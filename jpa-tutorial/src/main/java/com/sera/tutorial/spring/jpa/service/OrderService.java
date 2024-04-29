package com.sera.tutorial.spring.jpa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sera.tutorial.spring.jpa.entity.Order;
import com.sera.tutorial.spring.jpa.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderItemService orderItemService;

	public Order getOrder(Long id) {
		return orderRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
	}

	public String registerOrder() {
		return orderItemService.createOrderItem();
	}

	@Transactional
	public Order modifyAddress(Long id, String address) {
		Order order = getOrder(id);
		order.updateAddress(address);
		orderRepository.save(order);
		return order;
	}
}
