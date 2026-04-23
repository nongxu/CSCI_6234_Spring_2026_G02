package com.onlineorder.onlineorder.controller;

import com.onlineorder.onlineorder.entity.Order;
import com.onlineorder.onlineorder.model.OrderSummaryResponse;
import com.onlineorder.onlineorder.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/cart/summary")
    public ResponseEntity<?> getOrderSummary(HttpSession session) {
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
        OrderSummaryResponse summary = orderService.getOrderSummary(order);
        orderService.clearCart((Long) userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrderHistory(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        List<OrderSummaryResponse> history = orderService.getOrderHistory((Long) userId);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId, HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        if (!orderService.cancelOrder(orderId, (Long) userId)) {
            return ResponseEntity.badRequest().body("Cannot cancel this order");
        }
        return ResponseEntity.ok().build();
    }
}
