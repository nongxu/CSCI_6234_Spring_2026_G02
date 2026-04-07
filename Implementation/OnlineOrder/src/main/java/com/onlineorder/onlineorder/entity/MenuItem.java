package com.onlineorder.onlineorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("menu_items")
public class MenuItem {

    @Id
    private Long menuItemId;
    private Long restaurantId;
    private String name;
    private String description;
    private Double price;
    private String image;

    public MenuItem() {}

    public MenuItem(Long restaurantId, String name, String description, Double price, String image) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
