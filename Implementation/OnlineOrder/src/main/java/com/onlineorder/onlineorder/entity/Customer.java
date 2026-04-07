package com.onlineorder.onlineorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("customers")
public class Customer implements Persistable<Long> {

    @Id
    private Long userId;
    private String phoneNumber;

    @Transient
    private boolean isNew = false;

    public Customer() {}

    public Customer(Long userId, String phoneNumber) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Long getId() { return userId; }

    @Override
    public boolean isNew() { return isNew; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setNew(boolean isNew) { this.isNew = isNew; }
}
