package com.onlineorder.onlineorder.model;

import java.util.List;

public class RegisterRestaurantBody {

    private String restaurantName;
    private String address;
    private String phone;
    private List<MenuItemBody> menuItems;

    public RegisterRestaurantBody() {}

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<MenuItemBody> getMenuItems() { return menuItems; }
    public void setMenuItems(List<MenuItemBody> menuItems) { this.menuItems = menuItems; }

    public static class MenuItemBody {

        private String name;
        private String description;
        private Double price;

        public MenuItemBody() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }
}
