package com.onlineorder.onlineorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("restaurant_owners")
public class RestaurantOwner implements Persistable<Long> {

    @Id
    private Long userId;
    private String businessName;

    @Transient
    private boolean isNew = false;

    public RestaurantOwner() {}

    public RestaurantOwner(Long userId, String businessName) {
        this.userId = userId;
        this.businessName = businessName;
    }

    @Override
    public Long getId() { return userId; }

    @Override
    public boolean isNew() { return isNew; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public void setNew(boolean isNew) { this.isNew = isNew; }
}
