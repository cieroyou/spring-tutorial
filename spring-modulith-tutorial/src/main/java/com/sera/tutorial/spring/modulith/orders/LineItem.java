package com.sera.tutorial.spring.modulith.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "orders_line_items")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
// create table if not exists orders_line_items
// 	(
// 		id       serial primary key,
// 		product  int not null,
// 		quantity int not null,
// 		orders   int references orders (id)
// );
public class LineItem {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private Long product;
	private int quantity;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Order.class)
	@JoinColumn(name = "orders", nullable = false)
	private Order order;

	public LineItem(Long product, int quantity, Order order) {
		this.product = product;
		this.quantity = quantity;
		this.order = order;
	}
}
