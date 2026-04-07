package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.CartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long cartId);
}
