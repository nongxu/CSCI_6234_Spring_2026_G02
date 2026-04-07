package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
