package com.sera.tutorial.spring.modulith.products;

import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sera.tutorial.spring.modulith.ProductUpdateFailException;
import com.sera.tutorial.spring.modulith.orders.OrderPlacedEvent;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	//@Async: 이 어노테이션을 붙여야 Event를 publish 한 코드가 비동기로 동작한다. (확인 완료)
	@Async
	@EventListener
	@Retryable(
		value = { ProductUpdateFailException.class },
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000, multiplier = 2)
	)
	@ApplicationModuleListener
	void on(OrderPlacedEvent ope) throws Exception {
		log.info("Received order placed event: {}", ope);
		// if(retry == 3) {
		// 	System.out.println("starting [" + ope + "]");
		// 	Thread.sleep(5_000);
		// 	System.out.println("stopping [" + ope + "]");
		// }else{
		// 	throw new ProductUpdateFailException("재고시스템에서 에러가 발생함");
		// }
		// throw new RuntimeException("재고시스템에서 에러가 발생함");

		// Thread.sleep(5_000);
		// this.productsCollaborator.create(ope.order());
	}
}
