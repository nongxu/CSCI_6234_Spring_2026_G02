package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);
}
