package com.sera.tutorial.spring.jpa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sera.tutorial.spring.jpa.entity.OptimisticOrder;
import com.sera.tutorial.spring.jpa.repository.OptimisticOrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OptimisticOrderService {
    private final OptimisticOrderRepository orderRepository;

    public OptimisticOrder getOrder(Long id){
        return orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        // return orderRepository.findById(id).orElseThrow(
        //         () -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }

    @Transactional
    public OptimisticOrder modifyAddress(Long id, String address){
        OptimisticOrder order = getOrder(id);
        order.updateAddress(address);
        orderRepository.save(order);
        return order;
    }

    public void startDelivery(Long id){
        OptimisticOrder order = getOrder(id);
        order.startDelivery();
        orderRepository.save(order);
    }
}
