package com.onlineorder.onlineorder.controller;

import com.onlineorder.onlineorder.model.AddToCartBody;
import com.onlineorder.onlineorder.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart")
    public ResponseEntity<Double> addToCart(@RequestBody AddToCartBody body, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = (Long) session.getAttribute("userId");
        cartService.addItem(userId, body.getMenuItemId());
        Double total = cartService.calculateTotal(userId);
        return ResponseEntity.ok(total);
    }
}
