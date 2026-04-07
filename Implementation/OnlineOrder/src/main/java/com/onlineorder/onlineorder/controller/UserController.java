package com.onlineorder.onlineorder.controller;

import com.onlineorder.onlineorder.entity.User;
import com.onlineorder.onlineorder.model.LoginBody;
import com.onlineorder.onlineorder.model.SignUpBody;
import com.onlineorder.onlineorder.repository.UserRepository;
import com.onlineorder.onlineorder.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private final CustomerService customerService;
    private final UserRepository userRepository;

    public UserController(CustomerService customerService, UserRepository userRepository) {
        this.customerService = customerService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpBody body) {
        if (!customerService.validateCredentials(body)) {
            return ResponseEntity.badRequest().build();
        }
        customerService.register(body);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginBody body, HttpSession session) {
        if (!customerService.login(body, session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Returns the current session user's name and role.
     * role = "customer" | "owner"
     * Returns 401 if no active session.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser(HttpSession session) {
        // Check customer session
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            Optional<User> user = userRepository.findById((Long) userId);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "firstName", user.get().getFirstName(),
                        "lastName",  user.get().getLastName(),
                        "role",      "customer"
                ));
            }
        }

        // Check owner session
        Object ownerId = session.getAttribute("ownerId");
        if (ownerId != null) {
            Optional<User> user = userRepository.findById((Long) ownerId);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "firstName", user.get().getFirstName(),
                        "lastName",  user.get().getLastName(),
                        "role",      "owner"
                ));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Invalidates the current session (logs out both customer and owner).
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
