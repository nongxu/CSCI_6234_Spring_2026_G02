package com.onlineorder.onlineorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("restaurants")
public class Restaurant {

    @Id
    private Long restaurantId;
    private Long ownerId;
    private String name;
    private String address;
    private String phone;
    private String image;

    public Restaurant() {}

    public Restaurant(Long ownerId, String name, String address, String phone, String image) {
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
