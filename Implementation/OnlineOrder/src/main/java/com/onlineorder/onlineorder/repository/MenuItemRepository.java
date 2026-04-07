package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.MenuItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);
}
