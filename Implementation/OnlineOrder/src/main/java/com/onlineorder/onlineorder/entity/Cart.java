package com.onlineorder.onlineorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("carts")
public class Cart {

    @Id
    private Long cartId;
    private Long customerId;
    private String status;

    public Cart() {}

    public Cart(Long customerId, String status) {
        this.customerId = customerId;
        this.status = status;
    }

    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
