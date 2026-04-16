package com.onlineorder.onlineorder.model;

public class OrderSummaryItem {

    private String menuItemName;
    private Integer quantity;
    private Double price;

    public OrderSummaryItem() {}

    public OrderSummaryItem(String menuItemName, Integer quantity, Double price) {
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
