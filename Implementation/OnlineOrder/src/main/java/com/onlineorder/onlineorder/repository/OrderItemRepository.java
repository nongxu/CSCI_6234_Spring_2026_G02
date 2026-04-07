package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
