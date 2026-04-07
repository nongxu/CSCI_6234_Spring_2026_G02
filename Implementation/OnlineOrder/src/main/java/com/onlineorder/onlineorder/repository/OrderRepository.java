package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
