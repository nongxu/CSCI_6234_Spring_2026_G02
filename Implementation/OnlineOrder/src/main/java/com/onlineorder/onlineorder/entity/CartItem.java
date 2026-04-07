package com.onlineorder.onlineorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("cart_items")
public class CartItem {

    @Id
    private Long cartItemId;
    private Long cartId;
    private Long menuItemId;
    private Integer quantity;

    public CartItem() {}

    public CartItem(Long cartId, Long menuItemId, Integer quantity) {
        this.cartId = cartId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public Long getCartItemId() { return cartItemId; }
    public void setCartItemId(Long cartItemId) { this.cartItemId = cartItemId; }

    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }

    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
