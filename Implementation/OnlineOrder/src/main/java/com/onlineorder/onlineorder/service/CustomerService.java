package com.onlineorder.onlineorder.service;

import com.onlineorder.onlineorder.entity.Customer;
import com.onlineorder.onlineorder.entity.User;
import com.onlineorder.onlineorder.model.LoginBody;
import com.onlineorder.onlineorder.model.SignUpBody;
import com.onlineorder.onlineorder.repository.CustomerRepository;
import com.onlineorder.onlineorder.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public CustomerService(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public boolean validateCredentials(SignUpBody body) {
        if (body.getEmail() == null || body.getEmail().isBlank()) return false;
        if (body.getPassword() == null || body.getPassword().isBlank()) return false;
        if (body.getFirstName() == null || body.getFirstName().isBlank()) return false;
        if (body.getLastName() == null || body.getLastName().isBlank()) return false;
        if (userRepository.findByEmail(body.getEmail()).isPresent()) return false;
        return true;
    }

    public void register(SignUpBody body) {
        User user = new User(body.getEmail(), body.getPassword(), body.getFirstName(), body.getLastName());
        User savedUser = userRepository.save(user);

        Customer customer = new Customer(savedUser.getUserId(), null);
        customer.setNew(true);
        customerRepository.save(customer);
    }

    public boolean login(LoginBody body, HttpSession session) {
        Optional<User> optionalUser = userRepository.findByEmail(body.getEmail());
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        if (!user.getPassword().equals(body.getPassword())) return false;

        session.setAttribute("userId", user.getUserId());
        session.setAttribute("email", user.getEmail());
        return true;
    }
}
