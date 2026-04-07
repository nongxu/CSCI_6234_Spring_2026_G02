package com.onlineorder.onlineorder.model;

public class AddToCartBody {

    private Long menuItemId;

    public AddToCartBody() {}

    public AddToCartBody(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }
}
