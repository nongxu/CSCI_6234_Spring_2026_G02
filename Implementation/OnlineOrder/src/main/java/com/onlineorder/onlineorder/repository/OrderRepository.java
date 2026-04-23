package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);
}
