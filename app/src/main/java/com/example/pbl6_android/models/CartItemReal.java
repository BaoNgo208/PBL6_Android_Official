package com.example.pbl6_android.models;


import java.util.UUID;

public class CartItemReal  {
    private UUID cartItemId; // Bạn có thể sử dụng String nếu không có kiểu Guid trong Java
    private int quantity;
    private UUID cartId; // Cũng có thể sử dụng String
    private UUID productId; // Cũng có thể sử dụng String
    private Product product; // Tham chiếu đến lớp Product

    // Constructor
    public CartItemReal(UUID cartItemId, int quantity, UUID cartId, UUID productId, Product product) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.cartId = cartId;
        this.productId = productId;
        this.product = product;
    }

    // Getters and Setters
    public UUID getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(UUID cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }





}
