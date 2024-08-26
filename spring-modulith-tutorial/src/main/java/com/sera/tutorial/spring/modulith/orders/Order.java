package com.sera.tutorial.spring.modulith.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "orders")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Order {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
}
