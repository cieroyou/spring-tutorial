package com.sera.tutorial.spring.modulith.orders;

import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

record OrderRequest (List<LineItem> lineItems) {

}

@RequestMapping("/orders")
@RestController
class OrderController {

	private final OrderService orderService;

	OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	void placeOrder(@RequestBody OrderRequest request) {
		this.orderService.placeOrder(request);
	}
}

@RequiredArgsConstructor
@Service
@Slf4j
class OrderService {

	private final ApplicationEventPublisher publisher;

	private final OrderRepository repository;
	private final OrderLineItemRepository lineItemRepository;


	@Transactional
	void placeOrder(OrderRequest request) {
		var order = repository.save(new Order());
		var lineItems = request.lineItems().get(0);
		LineItem lineItem =
			new LineItem(lineItems.getProduct(), lineItems.getQuantity(),order);
		lineItemRepository.save(lineItem);
		log.info("saved [" + order + "]");
		publisher.publishEvent(
			new OrderPlacedEvent(order.getId(), lineItem.getQuantity(), lineItem.getProduct()));
	}
	void placeOrder(Order order) {
		var saved = this.repository.save(order);
		log.info("saved [" + saved + "]");
		// saved.lineItems()
		// 	.stream()
		// 	.map(li -> new OrderPlacedEvent(saved.id(), li.quantity(), li.product()))
		// 	.forEach(a -> {
		// 		log.info("publishing [" + a + "]");
		// 		publisher.publishEvent(a);
		// 		// this.publisher::publishEvent
		// 	});
	}

}


interface OrderRepository extends JpaRepository<Order, Long> {
}
//
// @Table("orders_line_items")
// record LineItem(@Id Integer id, int product, int quantity) {
// }
//
// @Table("orders")
// record Order(@Id Integer id, Set<LineItem> lineItems) {
// }