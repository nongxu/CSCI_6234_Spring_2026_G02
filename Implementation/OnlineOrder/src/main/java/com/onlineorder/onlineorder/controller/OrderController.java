package com.onlineorder.onlineorder.controller;

import com.onlineorder.onlineorder.entity.Order;
import com.onlineorder.onlineorder.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/cart/summary")
    public ResponseEntity<?> getCartSummary(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        if (!orderService.checkCart((Long) userId)) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }
        Double total = orderService.calculateTotal((Long) userId);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/cart/confirm")
    public ResponseEntity<?> confirmOrder(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        if (!orderService.checkCart((Long) userId)) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }
        Order order = orderService.createOrder((Long) userId);
        orderService.clearCart((Long) userId);
        return ResponseEntity.ok(order);
    }
}
