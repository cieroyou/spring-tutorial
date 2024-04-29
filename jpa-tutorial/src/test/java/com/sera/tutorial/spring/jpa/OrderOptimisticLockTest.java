package com.sera.tutorial.spring.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import com.sera.tutorial.spring.jpa.repository.OptimisticOrderRepository;
import com.sera.tutorial.spring.jpa.service.OptimisticOrderService;
import com.sera.tutorial.spring.jpa.entity.*;

/**
 * SpringBootTest를 사용하는 경우, 영속성 컨텍스트를 활용하려면 @Transactional 을 사용해야 한다.
 */
@SpringBootTest
public class OrderOptimisticLockTest {
    @Autowired
    OptimisticOrderRepository optimisticOrderRepository;

    @Autowired
    OptimisticOrderService optimisticOrderService;

    @BeforeEach
    void setUp() {
        optimisticOrderRepository.deleteAll();
    }

    @Transactional
    @Test
    void 영속성테스트() {
        OptimisticOrder order = optimisticOrderRepository.save(new OptimisticOrder("내 집은 영등포"));
        Assertions.assertEquals(0, (int) order.getVersion());

        OptimisticOrder retrievedOrder = optimisticOrderService.getOrder(order.getId());
        Assertions.assertEquals(order, retrievedOrder);
    }

    @Description("version이 0인 상태에서 다른 트랜잭션이 수정을 할 때마다, version이 1씩 증가한다.")
    @Test
    void testVersion_optimistic_force_increment() {
        OptimisticOrder order = optimisticOrderRepository.save(new OptimisticOrder("내 집은 영등포"));
        Assertions.assertEquals(0, (int) order.getVersion());

        OptimisticOrder modifiedOrder = optimisticOrderService.modifyAddress(order.getId(), "내 집은 강남");
        Assertions.assertEquals(1, (int) modifiedOrder.getVersion());
    }

    @Description("레코드에 동시에 여러쓰레드가 수정을 하려는 경우, OptimisticLockingFailureException이 발생한다.")
    @Test
    void testOptimisticLockingFailureException() throws InterruptedException, ExecutionException {
        OptimisticOrder order = optimisticOrderRepository.save(new OptimisticOrder("내 집은 영등포"));
        Assertions.assertEquals(0, (int) order.getVersion());
        CompletableFuture<?> future1 = CompletableFuture.supplyAsync(() ->
                optimisticOrderService.modifyAddress(order.getId(), "내 집은 강북")
        );
        CompletableFuture<?> future2 = CompletableFuture.runAsync(() ->
                optimisticOrderService.startDelivery(order.getId())
        );

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(future1, future2);
        Exception result = null;
        try {
            completableFuture.get();
        } catch (ExecutionException e) {
            result = (Exception) e.getCause();
        }
        assertTrue(result instanceof OptimisticLockingFailureException);
    }

//    @Description("레코드에 동시에 여러쓰레드가 수정을 하려는 경우, OptimisticLockingFailureException이 발생한다.")
//    @Test
//    void test() throws InterruptedException, ExecutionException {
//        OptimisticOrder order = optimisticOrderRepository.save(new OptimisticOrder("내 집은 영등포"));
////        CompletableFuture<?> future1 = CompletableFuture.supplyAsync(() ->
////                optimisticOrderService.modifyAddress(order.getId(), "내 집은 강북")
////        );
////        https://chaewsscode.tistory.com/181
//        CompletableFuture<?> future2 = CompletableFuture.runAsync(() ->
//                optimisticOrderService.getOrder(order.getId())
//        );
//
//        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(future1, future2);
//        Exception result = null;
//        try {
//            completableFuture.get();
//        } catch (ExecutionException e) {
//            result = (Exception) e.getCause();
//        }
//        assertTrue(result instanceof OptimisticLockException);
//    }
}
