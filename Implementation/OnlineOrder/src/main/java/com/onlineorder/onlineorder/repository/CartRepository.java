package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {

    Optional<Cart> findByCustomerId(Long customerId);
}
