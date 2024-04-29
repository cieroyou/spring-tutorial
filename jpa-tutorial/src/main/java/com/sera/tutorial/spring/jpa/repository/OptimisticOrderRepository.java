package com.sera.tutorial.spring.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sera.tutorial.spring.jpa.entity.OptimisticOrder;

public interface OptimisticOrderRepository extends JpaRepository<OptimisticOrder, Long> {


}
