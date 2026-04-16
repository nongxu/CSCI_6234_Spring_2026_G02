package com.onlineorder.onlineorder.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryResponse {

    private Long orderId;
    private List<OrderSummaryItem> items;
    private Double totalPrice;
    private String status;
    private LocalDateTime createdAt;

    public OrderSummaryResponse() {}

    public OrderSummaryResponse(Long orderId, List<OrderSummaryItem> items, Double totalPrice, String status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public List<OrderSummaryItem> getItems() { return items; }
    public void setItems(List<OrderSummaryItem> items) { this.items = items; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
