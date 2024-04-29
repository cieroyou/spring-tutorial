package com.sera.tutorial.spring.jpa.service;

import org.springframework.stereotype.Service;

import com.sera.tutorial.spring.jpa.repository.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemService {

	private final OrderRepository orderRepository;
	public String createOrderItem(){
		log.info("createOrderItem");
		return "abcd";
	}
}
