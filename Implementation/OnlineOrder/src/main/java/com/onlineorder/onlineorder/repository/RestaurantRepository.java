package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    List<Restaurant> findAll();
}
