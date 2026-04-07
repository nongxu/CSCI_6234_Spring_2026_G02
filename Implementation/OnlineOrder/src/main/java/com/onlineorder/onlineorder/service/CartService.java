package com.onlineorder.onlineorder.service;

import com.onlineorder.onlineorder.entity.Cart;
import com.onlineorder.onlineorder.entity.CartItem;
import com.onlineorder.onlineorder.repository.CartItemRepository;
import com.onlineorder.onlineorder.repository.CartRepository;
import com.onlineorder.onlineorder.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Cart getOrCreateCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> cartRepository.save(new Cart(customerId, "ACTIVE")));
    }

    public void addItem(Long customerId, Long menuItemId) {
        Cart cart = getOrCreateCart(customerId);
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

        Optional<CartItem> existing = cartItems.stream()
                .filter(item -> item.getMenuItemId().equals(menuItemId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.save(new CartItem(cart.getCartId(), menuItemId, 1));
        }
    }

    public Double calculateTotal(Long customerId) {
        Cart cart = getOrCreateCart(customerId);
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

        return cartItems.stream()
                .mapToDouble(cartItem -> menuItemRepository.findById(cartItem.getMenuItemId())
                        .map(menuItem -> menuItem.getPrice() * cartItem.getQuantity())
                        .orElse(0.0))
                .sum();
    }
}
