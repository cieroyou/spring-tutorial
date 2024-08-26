package com.sera.tutorial.spring.modulith.products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record ProductUpdatedEvent(String productId, String result) {
}
