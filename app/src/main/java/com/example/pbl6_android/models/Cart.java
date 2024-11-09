package com.example.pbl6_android.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Cart {
    private UUID cartId;
    private UUID userId;
    private String createdDate;
    private List<CartItemReal> cartItems;

    // Getters and Setters
    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String  getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<CartItemReal> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemReal> cartItems) {
        this.cartItems = cartItems;
    }
}
