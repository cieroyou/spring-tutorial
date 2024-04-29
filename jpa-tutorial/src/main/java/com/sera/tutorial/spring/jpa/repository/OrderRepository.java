package com.sera.tutorial.spring.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sera.tutorial.spring.jpa.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
