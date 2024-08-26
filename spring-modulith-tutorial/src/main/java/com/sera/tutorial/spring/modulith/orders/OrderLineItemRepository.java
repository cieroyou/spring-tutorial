package com.sera.tutorial.spring.modulith.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

interface OrderLineItemRepository extends JpaRepository<LineItem, Integer> {
}
//
// @Table("orders_line_items")
// record LineItem(@Id Integer id, int product, int quantity) {
// }
//
// @Table("orders")
// record Order(@Id Integer id, Set<LineItem> lineItems) {
// }