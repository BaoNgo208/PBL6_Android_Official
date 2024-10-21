package com.example.pbl6_android.models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class OrderDetail {
    public UUID OrderDetailId ;
    @SerializedName("Quantity")
    public int Quantity ;

    public UUID getOrderDetailId() {
        return OrderDetailId;
    }

    public void setOrderDetailId(UUID orderDetailId) {
        OrderDetailId = orderDetailId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderDetail(int quantity, UUID productId) {
        Quantity = quantity;
        ProductId = productId;
    }

    public OrderDetail(int quantity, Product product) {
        Quantity = quantity;
        this.product = product;
    }
    @SerializedName("ProductId")
    public UUID ProductId ;

}
