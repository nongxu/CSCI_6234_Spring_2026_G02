package com.onlineorder.onlineorder.service;

import com.onlineorder.onlineorder.entity.Cart;
import com.onlineorder.onlineorder.entity.CartItem;
import com.onlineorder.onlineorder.entity.Order;
import com.onlineorder.onlineorder.entity.OrderItem;
import com.onlineorder.onlineorder.repository.CartItemRepository;
import com.onlineorder.onlineorder.repository.CartRepository;
import com.onlineorder.onlineorder.repository.MenuItemRepository;
import com.onlineorder.onlineorder.repository.OrderItemRepository;
import com.onlineorder.onlineorder.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        MenuItemRepository menuItemRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public boolean checkCart(Long customerId) {
        Optional<Cart> optionalCart = cartRepository.findByCustomerId(customerId);
        if (optionalCart.isEmpty()) return false;
        List<CartItem> cartItems = cartItemRepository.findByCartId(optionalCart.get().getCartId());
        return !cartItems.isEmpty();
    }

    public Double calculateTotal(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).get();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

        return cartItems.stream()
                .mapToDouble(cartItem -> menuItemRepository.findById(cartItem.getMenuItemId())
                        .map(menuItem -> menuItem.getPrice() * cartItem.getQuantity())
                        .orElse(0.0))
                .sum();
    }

    public Order createOrder(Long customerId) {
        Double totalPrice = calculateTotal(customerId);
        Order order = new Order(customerId, totalPrice, "PENDING", LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        Cart cart = cartRepository.findByCustomerId(customerId).get();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

        for (CartItem cartItem : cartItems) {
            Double price = menuItemRepository.findById(cartItem.getMenuItemId())
                    .map(menuItem -> menuItem.getPrice())
                    .orElse(0.0);
            orderItemRepository.save(new OrderItem(savedOrder.getOrderId(), cartItem.getMenuItemId(), cartItem.getQuantity(), price));
        }

        return savedOrder;
    }

    public void clearCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).get();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

        for (CartItem cartItem : cartItems) {
            cartItemRepository.deleteById(cartItem.getCartItemId());
        }
    }
}
