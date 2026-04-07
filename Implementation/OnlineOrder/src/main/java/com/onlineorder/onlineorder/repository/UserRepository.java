package com.onlineorder.onlineorder.repository;

import com.onlineorder.onlineorder.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
