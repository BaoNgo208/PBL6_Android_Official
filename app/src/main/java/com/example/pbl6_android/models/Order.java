package com.example.pbl6_android.models;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
public class Order {

    private UUID orderId;

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    private String promotionId;

    public Order( String status, List<OrderDetail> orderDetails,String promotionId) {
        this.promotionId = promotionId;
        this.orderDetails = orderDetails;
        this.status = status;
    }

    private String orderDate;
    private List<OrderDetail> orderDetails;

    private double totalAmount; // Use BigDecimal for precise amount

    public Order(String status, List<OrderDetail> orderDetails) {
        this.status = status;
        this.orderDetails = orderDetails;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String  getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    private String status;

    private User user;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Order(UUID orderId, String orderDate, int totalAmount, String status, User user, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.user = user;
       this.orderDetails = orderDetails;
    }

    public Order(UUID orderId, String orderDate, float totalAmount, String status, User user, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;

        this.status = status;
        this.user = user;
        this.orderDetails = orderDetails;
    }
}
